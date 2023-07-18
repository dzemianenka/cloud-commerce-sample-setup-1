package firework.service.impl;


import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;
import firework.mapper.ProductResponseMapper;
import firework.model.product.get.ProductSearchEntry;
import firework.model.product.get.ProductsSearchData;
import firework.service.BusinessStoreService;
import firework.service.FwApiCredentialsService;
import firework.service.FwProductService;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.EnumSet;

@RequiredArgsConstructor
public class FwProductServiceImpl implements FwProductService {

    private static final EnumSet<ProductOption> PRODUCT_OPTIONS_SET = EnumSet.allOf(ProductOption.class);

    private final ProductSearchFacade<ProductData> productSearchFacade;
    private final ProductFacade productFacade;
    private final Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;
    private final BaseSiteService baseSiteService;
    private final CommerceCommonI18NService commerceCommonI18NService;
    private final FwApiCredentialsService fwApiCredentialsService;
    private final BusinessStoreService businessStoreService;
    private final ProductResponseMapper productResponseMapper;

    @Override
    public ProductsSearchData getProductsForStore(@NotBlank String businessId, @NotBlank String businessStoreId, @Positive int page, @Positive int pageSize) {

        setup(businessStoreId);
        final var searchQueryData = new SolrSearchQueryData();
        final PageableData pageable = createPageableData(page - 1, pageSize);

        final ProductSearchPageData<SearchStateData, ProductData> searchResult = productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
        return productResponseMapper.createProductSearchData(businessStoreId, searchResult);
    }

    @Override
    public ProductSearchEntry getProductForStoreByProductExtId(String productExtId, String businessStoreId) {
        setup(businessStoreId);
        final var product = productFacade.getProductForCodeAndOptions(productExtId, PRODUCT_OPTIONS_SET);
        final var fireworkBusinessStore = businessStoreService.getBusinessStoreByStoreId(businessStoreId);
        return productResponseMapper.createProductEntry(product, fireworkBusinessStore);
    }

    private void setup(String businessStoreId) {
        final var baseSiteId = fwApiCredentialsService.getFwApiCredentialsByStoreId(businessStoreId).getBaseSiteId();
        baseSiteService.setCurrentBaseSite(baseSiteId, true);
        commerceCommonI18NService.setCurrentCurrency(commerceCommonI18NService.getDefaultCurrency());
    }

    protected PageableData createPageableData(final int currentPage, final int pageSize) {
        final PageableData pageable = new PageableData();
        pageable.setCurrentPage(currentPage);
        pageable.setPageSize(pageSize);
        return pageable;
    }
}
