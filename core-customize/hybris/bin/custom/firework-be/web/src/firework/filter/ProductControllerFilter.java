package firework.filter;

import com.google.gson.Gson;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import firework.exception.handler.FireworkExceptionDto;
import firework.service.FwApiCredentialsService;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext;

@Log4j
@Component
public class ProductControllerFilter extends GenericFilterBean {

    private FwApiCredentialsService fwApiCredentialsService;

    @Override
    protected void initFilterBean() throws ServletException {
        final var webApplicationContext = getWebApplicationContext(getServletContext());
        this.fwApiCredentialsService = webApplicationContext.getBean("fwApiCredentialsService", FwApiCredentialsService.class);
        super.initFilterBean();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final var request = (HttpServletRequest) servletRequest;
        final var response = (HttpServletResponse) servletResponse;

        final var timestampHeader = request.getHeader("X-fw-timestamp");
        if (isBlank(timestampHeader)) {
            sendError(response, "Timestamp header is empty", SC_UNAUTHORIZED);
            return;
        }
        final var currentUnixTime = System.currentTimeMillis() / 1000L;
        final var timestampParameterValue = servletRequest.getParameter("timestamp");
        if (isBlank(timestampParameterValue)) {
            sendError(response, "Timestamp parameter is empty", SC_UNAUTHORIZED);
            return;
        }
        final var requestParamTimestamp = parseLong(timestampParameterValue);
        final var requestHeaderTimestamp = parseLong(timestampHeader);
        final var headerTimeDifferInSeconds = currentUnixTime - requestHeaderTimestamp;
        final var paramTimeDifferInSeconds = currentUnixTime - requestParamTimestamp;

        if (headerTimeDifferInSeconds > 900 || paramTimeDifferInSeconds > 900) {
            sendError(response, "Please check timestamp validity", SC_UNAUTHORIZED);
            return;
        }

        final var hmacHashedOutput = request.getHeader("X-fw-hmac-sha512");
        if (isBlank(hmacHashedOutput)) {
            sendError(response, "Hmac header is empty", SC_UNAUTHORIZED);
            return;
        }
        final var decodedHmacHashedOutput = new String(Base64.getDecoder().decode(hmacHashedOutput));
        final var pattern = Pattern.compile("base64_encode\\(HMACSHA512\\(\".+?\",\\s*\"(.+?)\"\\)\\)");
        final var matcher = pattern.matcher(decodedHmacHashedOutput);
        try {
            if (matcher.find()) {
                final var requestURI = request.getRequestURI();
                String businessStoreId = extractPathVariable(requestURI, "/store/");
                final var hmacSigningKey = fwApiCredentialsService.getFwApiCredentialsByStoreId(businessStoreId).getHmacSigningKey();
                final var hmacSecretFromHeader = matcher.group(1);
                final var correctHmacSecret = StringUtils.equals(hmacSecretFromHeader, hmacSigningKey);
                if (correctHmacSecret) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    sendError(response, "Please check hmac header validity", SC_FORBIDDEN);
                }
            } else {
                sendError(response, "Please check hmac header validity", SC_FORBIDDEN);
            }
        } catch (ModelNotFoundException e) {
            sendError(response, e.getMessage(), SC_NOT_FOUND);
        }
    }

    private void sendError(HttpServletResponse response, String errorMessage, int statusCode) throws IOException {
        final var dto = new FireworkExceptionDto(errorMessage);
        response.setStatus(statusCode);
        response.setContentType(APPLICATION_JSON_VALUE);
        final var responseWriter = response.getWriter();
        responseWriter.write(new Gson().toJson(dto));
        responseWriter.flush();
    }

    private String extractPathVariable(String requestURI, String prefix) {
        int startIndex = requestURI.indexOf(prefix);
        if (startIndex != -1) {
            startIndex += prefix.length();
            int endIndex = requestURI.indexOf("/", startIndex);
            if (endIndex != -1) {
                return requestURI.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    @Override
    public void destroy() {
        log.warn("Destructing filter: %s".formatted(this.getClass().getName()));
    }
}
