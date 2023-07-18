package firework.mapper.impl;

import firework.dto.FireworkAuthorizationResponseDto;
import firework.dto.FireworkOAuthWsDTO;
import firework.dto.FireworkResponseDto;
import firework.mapper.FwOAuthAppMapper;
import firework.model.FwOAuthAppModel;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.time.Duration.ofSeconds;
import static java.util.Objects.nonNull;

@Component
public class FwOAuthAppMapperImpl implements FwOAuthAppMapper {
    @Override
    public FireworkOAuthWsDTO toWsDTO(FwOAuthAppModel model) {
        return FireworkOAuthWsDTO.builder()
                .pk(model.getPk())
                .oAuthAppId(model.getOAuthAppId())
                .clientId(model.getClientId())
                .clientSecret(model.getClientSecret())
                .accessToken(model.getAccessToken())
                .refreshToken(model.getRefreshToken())
                .tokenExpiresIn(model.getTokenExpiresIn())
                .refreshTokenExpiresIn(model.getRefreshTokenExpiresIn())
                .fireworkBusinessId(model.getFireworkBusinessId())
                .build();
    }

    @Override
    public FwOAuthAppModel toModel(FireworkOAuthWsDTO fwOAuthWsDTO) {
        FwOAuthAppModel target = new FwOAuthAppModel();

        target.setOAuthAppId(fwOAuthWsDTO.getOAuthAppId());
        target.setClientId(fwOAuthWsDTO.getClientId());
        target.setClientSecret(fwOAuthWsDTO.getClientSecret());
        target.setAccessToken(fwOAuthWsDTO.getAccessToken());
        target.setRefreshToken(fwOAuthWsDTO.getRefreshToken());
        target.setTokenExpiresIn(fwOAuthWsDTO.getTokenExpiresIn());
        target.setRefreshTokenExpiresIn(fwOAuthWsDTO.getRefreshTokenExpiresIn());

        return target;
    }


    @Override
    public FwOAuthAppModel mergeToModel(FwOAuthAppModel targetToMerge, FireworkOAuthWsDTO fwOAuthWsDTO) {

        targetToMerge.setOAuthAppId(fwOAuthWsDTO.getOAuthAppId());
        targetToMerge.setClientId(fwOAuthWsDTO.getClientId());
        targetToMerge.setClientSecret(fwOAuthWsDTO.getClientSecret());
        targetToMerge.setAccessToken(fwOAuthWsDTO.getAccessToken());
        targetToMerge.setRefreshToken(fwOAuthWsDTO.getRefreshToken());
        targetToMerge.setTokenExpiresIn(fwOAuthWsDTO.getTokenExpiresIn());
        targetToMerge.setRefreshTokenExpiresIn(fwOAuthWsDTO.getRefreshTokenExpiresIn());
        targetToMerge.setFireworkBusinessId(fwOAuthWsDTO.getFireworkBusinessId());

        return targetToMerge;
    }

    @Override
    public FireworkOAuthWsDTO mergeExternalOAuthToWsDTO(FireworkOAuthWsDTO target, FireworkResponseDto response) {
        if (nonNull(response.getId())) {
            target.setOAuthAppId(response.getId());
        }
        if (nonNull(response.getClientId())) {
            target.setClientId(response.getClientId());
        }
        if (nonNull(response.getClientSecret())) {
            target.setClientSecret(response.getClientSecret());
        }

        return target;
    }

    @Override
    public void mergeExternalCredentialsToWsDTO(FireworkOAuthWsDTO target, FireworkAuthorizationResponseDto response) {
        if (nonNull(response.getAccessToken())) {
            target.setAccessToken(response.getAccessToken());
        }
        if (nonNull(response.getRefreshToken())) {
            target.setRefreshToken(response.getRefreshToken());
        }
        if (nonNull(response.getTokenExpiresIn())) {
            var expirationPeriod = ofSeconds(response.getTokenExpiresIn()).toMillis();
            var tokenExpirationDate = new Date(response.getCreatedAt().getTime() + expirationPeriod);
            target.setTokenExpiresIn(tokenExpirationDate);
        }
        if (nonNull(response.getRefreshTokenExpiresIn())) {
            var expirationPeriod = ofSeconds(response.getRefreshTokenExpiresIn()).toMillis();
            var refreshTokenExpirationDate = new Date(response.getCreatedAt().getTime() + expirationPeriod);
            target.setRefreshTokenExpiresIn(refreshTokenExpirationDate);
        }
    }
}
