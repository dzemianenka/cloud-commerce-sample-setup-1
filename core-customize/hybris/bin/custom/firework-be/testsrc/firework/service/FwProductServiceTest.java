package firework.service;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;
import firework.dto.FireworkCredentialsWsDTO;
import firework.mapper.ProductResponseMapper;
import firework.model.businessstore.get.BusinessStore;
import firework.model.product.get.ProductSearchEntry;
import firework.model.product.get.ProductsSearchData;
import firework.service.impl.FwProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
public class FwProductServiceTest {

    private static final String BUSINESS_ID = "businessId";
    private static final String BUSINESS_STORE_ID = "businessStoreId";
    private static final EnumSet<ProductOption> PRODUCT_OPTIONS_SET = EnumSet.allOf(ProductOption.class);

    @InjectMocks
    private FwProductServiceImpl productService;
    @Mock
    private ProductSearchFacade<ProductData> productSearchFacade;
    @Mock
    private ProductFacade productFacade;
    @Mock
    private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;
    @Mock
    private BaseSiteService baseSiteService;
    @Mock
    private CommerceCommonI18NService commerceCommonI18NService;
    @Mock
    private FwApiCredentialsService fwApiCredentialsService;
    @Mock
    private BusinessStoreService businessStoreService;
    @Mock
    private ProductResponseMapper productResponseMapper;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getProductsForStore_givenCorrectData_shouldReturnProductsForStore() {

        //Given
        final var page = 1;
        final var pageSize = 1;

        setupMethodMock();

        final var searchStateData = mock(SearchStateData.class);
        final var productSearchPageData = mock(ProductSearchPageData.class);
        final var productsSearchData = mock(ProductsSearchData.class);

        when(solrSearchStateConverter.convert(any(SolrSearchQueryData.class))).thenReturn(searchStateData);
        when(productSearchFacade.textSearch(any(SearchStateData.class), any(PageableData.class))).thenReturn(productSearchPageData);
        when(productResponseMapper.createProductSearchData(BUSINESS_STORE_ID, productSearchPageData)).thenReturn(productsSearchData);

        //When
        final var productsForStore = productService.getProductsForStore(BUSINESS_ID, BUSINESS_STORE_ID, page, pageSize);

        //Then
        assertEquals(productsSearchData, productsForStore);
    }

    @Test
    public void getProductForStoreByProductExtId_givenCorrectData_shouldReturnData() {

        //Given
        final var productExtId = "productExtId";

        setupMethodMock();

        final var productData = mock(ProductData.class);
        final var businessStore = mock(BusinessStore.class);
        final var productSearchEntry = mock(ProductSearchEntry.class);

        when(productFacade.getProductForCodeAndOptions(productExtId, PRODUCT_OPTIONS_SET)).thenReturn(productData);
        when(businessStoreService.getBusinessStoreByStoreId(BUSINESS_STORE_ID)).thenReturn(businessStore);
        when(productResponseMapper.createProductEntry(productData, businessStore)).thenReturn(productSearchEntry);

        //When
        final var product = productService.getProductForStoreByProductExtId(productExtId, BUSINESS_STORE_ID);

        //Then
        assertEquals(productSearchEntry, product);
    }

    private void setupMethodMock() {
        final var baseSiteId = "baseSiteId";
        final var fireworkCredentialsWsDTO = mock(FireworkCredentialsWsDTO.class);
        final var currencyModel = mock(CurrencyModel.class);

        when(fireworkCredentialsWsDTO.getBaseSiteId()).thenReturn(baseSiteId);
        when(fwApiCredentialsService.getFwApiCredentialsByStoreId(FwProductServiceTest.BUSINESS_STORE_ID)).thenReturn(fireworkCredentialsWsDTO);
        doNothing().when(baseSiteService).setCurrentBaseSite(baseSiteId, true);
        when(commerceCommonI18NService.getDefaultCurrency()).thenReturn(currencyModel);
        doNothing().when(commerceCommonI18NService).setCurrentCurrency(currencyModel);
    }
}
