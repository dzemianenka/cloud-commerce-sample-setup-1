package firework.mapper;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.BaseOptionData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import firework.mapper.impl.ProductResponseMapperImpl;
import firework.model.businessstore.get.BusinessStore;
import firework.model.product.get.ProductSearchEntry;
import firework.model.product.get.ProductSearchImageEntry;
import firework.model.product.get.ProductSearchUnitEntry;
import firework.service.BusinessStoreService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static de.hybris.platform.commercefacades.product.data.ImageDataType.PRIMARY;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
public class ProductResponseMapperTest {

    public static final String CURRENCY = "EUR";
    public static final String PRODUCT_CODE = "13401414";
    public static final String PRODUCT_URL = "/product";
    public static final String PRODUCT_NAME = "T-shirt";
    public static final String PRODUCT_DESCRIPTION = "The best T-shirt";
    public static final String VARIANT_OPTION_QUALIFIER = "style";
    public static final String BASE_OPTION_1_VARIANT_OPTION_QUALIFIER = "style";
    public static final String BASE_OPTION_2_VARIANT_OPTION_QUALIFIER = "color";
    public static final String BASE_OPTION_1_CODE = "3461293";
    public static final String BASE_OPTION_2_CODE = "19746319";
    public static final String VARIANT_OPTION_DATA_CODE = "12931419";
    public static final String VARIANT_OPTION_QUALIFIER_VALUE = "convenient";
    public static final String BASE_OPTION_1_VARIANT_OPTION_QUALIFIER_VALUE = "convenient";
    public static final String BASE_OPTION_2_VARIANT_OPTION_QUALIFIER_VALUE = "black";
    public static final String PRODUCT_IMAGE_DATA_ALT_TEXT = "product front";
    public static final String PRODUCT_IMAGE_DATA_URL = "/media";
    public static final String BASE_OPTION_2_VARIANT_OPTION_QUALIFIER_IMAGE_ALT_TEXT = "product back";
    public static final String BASE_OPTION_2_VARIANT_OPTION_QUALIFIER_IMAGE_URL = "/media/2";
    public static final BigDecimal VARIANT_OPTION_DATA_PRICE_DATA_VALUE = BigDecimal.ONE;
    public static final BigDecimal BASE_OPTION_1_VARIANT_OPTION_PRICE_DATA = TEN;
    public static final BigDecimal BASE_OPTION_2_VARIANT_OPTION_PRICE_DATA = ZERO;
    public static final String BUSINESS_STORE_URL = "https://example.com";
    public static final String BUSINESS_STORE_ID = "wvd2va";
    public static final String BUSINESS_STORE_NAME = "apparel-uk";
    public static final String BUSINESS_STORE_UID = "https://example.com";
    public static final long STOCK_COUNT = 15L;
    public static final String VARIANT_OPTION_DATA_URL = "/variant";
    public static final String BASE_OPTION_1_VARIANT_OPTION_URL = "/base/1";
    public static final String BASE_OPTION_2_VARIANT_OPTION_URL = "/base/2";
    public static final String VARIANT_OPTION_DATA_IMAGE_URL = "/media";
    public static final String VARIANT_OPTION_DATA_IMAGE_ALT_TEXT = "product full";
    public static final int PAGE_SIZE = 1;
    public static final int CURRENT_PAGE = 1;
    public static final long TOTAL_NUMBER_OF_RESULTS = 128;
    public static final int NUMBER_OF_PAGES = 128;
    @InjectMocks
    private ProductResponseMapperImpl productResponseMapper;
    @Mock
    private ProductFacade productFacade;
    @Mock
    private BusinessStoreService businessStoreService;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createProductEntry_givenCorrectData_shouldReturnData1() {

        //Given
        final var mockProductData = createMockProductData(true, false);
        final var mockBusinessStore = createMockBusinessStore();
        mockProductFacadeBehaviour();

        //When
        final var productEntry = productResponseMapper.createProductEntry(mockProductData, mockBusinessStore);

        //Then
        assertProductDataSimpleFields(productEntry, 1);
        assertProductUnitEntryFromVariantOption(productEntry.getProductUnits().get(0));
        assertTrue(productEntry.getProductOptions().contains(VARIANT_OPTION_QUALIFIER));
        assertProductImageEntry(productEntry.getProductImages().get(0),
                VARIANT_OPTION_DATA_IMAGE_ALT_TEXT, VARIANT_OPTION_DATA_CODE, VARIANT_OPTION_QUALIFIER_VALUE);
    }

    @Test
    public void createProductEntry_givenCorrectData_shouldReturnData2() {

        //Given
        final var mockProductData = createMockProductData(false, true);
        final var mockBusinessStore = createMockBusinessStore();
        mockProductFacadeBehaviour();

        //When
        final var productEntry = productResponseMapper.createProductEntry(mockProductData, mockBusinessStore);

        //Then
        assertProductDataSimpleFields(productEntry, 1);
        assertProductSearchEntryFromBaseOption(productEntry.getProductUnits().get(0));
        assertTrue(productEntry.getProductOptions().contains(BASE_OPTION_1_VARIANT_OPTION_QUALIFIER));
        assertProductImageEntry(productEntry.getProductImages().get(0),
                PRODUCT_IMAGE_DATA_ALT_TEXT, BASE_OPTION_1_CODE, BASE_OPTION_1_VARIANT_OPTION_QUALIFIER_VALUE);
    }

    @Test
    public void createProductEntry_givenCorrectData_shouldReturnData3() {

        //Given
        final var mockProductData = createMockProductData(false, false);
        final var mockBusinessStore = createMockBusinessStore();
        mockProductFacadeBehaviour();

        //When
        final var productEntry = productResponseMapper.createProductEntry(mockProductData, mockBusinessStore);

        //Then
        assertProductDataSimpleFields(productEntry, 0);
    }

    @Test
    public void createProductSearchData_givenCorrectData_shouldReturnData() {

        //Given
        final var mockProductSearchData = createMockProductSearchData();
        final var mockBusinessStore = createMockBusinessStore();
        when(businessStoreService.getBusinessStoreByStoreId(BUSINESS_STORE_ID)).thenReturn(mockBusinessStore);
        mockProductFacadeBehaviour();

        //When
        final var productSearchData = productResponseMapper.createProductSearchData(BUSINESS_STORE_ID, mockProductSearchData);

        assertNotNull(productSearchData);
        assertNotNull(productSearchData.getEntries());
        assertEquals(1, productSearchData.getEntries().size());
        final var productEntry = productSearchData.getEntries().get(0);
        assertProductDataSimpleFields(productEntry, 1);
        assertProductUnitEntryFromVariantOption(productEntry.getProductUnits().get(0));
        assertTrue(productEntry.getProductOptions().contains(VARIANT_OPTION_QUALIFIER));
        assertProductImageEntry(productEntry.getProductImages().get(0),
                VARIANT_OPTION_DATA_IMAGE_ALT_TEXT, VARIANT_OPTION_DATA_CODE, VARIANT_OPTION_QUALIFIER_VALUE);
    }

    private void assertProductImageEntry(ProductSearchImageEntry productSearchImageEntry, String imageExtId, String unitIdentifier, String unitName) {
        assertEquals(imageExtId, productSearchImageEntry.getImageExtId());
        assertEquals(0, productSearchImageEntry.getImagePosition());
        assertEquals(BUSINESS_STORE_URL + VARIANT_OPTION_DATA_IMAGE_URL, productSearchImageEntry.getImageSrc());
        final var unitIdentifiers = productSearchImageEntry.getUnitIdentifiers();
        assertEquals(1, unitIdentifiers.size());
        assertEquals(unitIdentifier, unitIdentifiers.get(0));
        final var unitNames = productSearchImageEntry.getUnitNames();
        assertEquals(1, unitNames.size());
        assertEquals(unitName, unitNames.get(0));
    }

    private void assertProductUnitEntryFromVariantOption(ProductSearchUnitEntry productSearchUnitEntry) {
        assertEquals(VARIANT_OPTION_DATA_CODE, productSearchUnitEntry.getUnitExtId());
        assertEquals(VARIANT_OPTION_QUALIFIER_VALUE, productSearchUnitEntry.getUnitName());
        assertEquals(0, productSearchUnitEntry.getUnitPosition());
        assertEquals(VARIANT_OPTION_DATA_PRICE_DATA_VALUE.toString(), productSearchUnitEntry.getUnitOriginalPrice());
        assertEquals(VARIANT_OPTION_DATA_PRICE_DATA_VALUE.toString(), productSearchUnitEntry.getUnitPrice());
        assertEquals(STOCK_COUNT, productSearchUnitEntry.getUnitQuantity());
        assertEquals(BUSINESS_STORE_URL + VARIANT_OPTION_DATA_URL, productSearchUnitEntry.getUnitUrl());
        assertEquals(1, productSearchUnitEntry.getUnitOptions().size());
        final var productSearchUnitOptionEntry = productSearchUnitEntry.getUnitOptions().get(0);
        assertEquals(VARIANT_OPTION_QUALIFIER, productSearchUnitOptionEntry.getName());
        assertEquals(VARIANT_OPTION_QUALIFIER_VALUE, productSearchUnitOptionEntry.getValue());
    }

    private void assertProductSearchEntryFromBaseOption(ProductSearchUnitEntry productSearchUnitEntry) {
        assertEquals(BASE_OPTION_1_CODE, productSearchUnitEntry.getUnitExtId());
        assertEquals(BASE_OPTION_1_VARIANT_OPTION_QUALIFIER_VALUE, productSearchUnitEntry.getUnitName());
        assertEquals(0, productSearchUnitEntry.getUnitPosition());
        assertEquals(BASE_OPTION_1_VARIANT_OPTION_PRICE_DATA.toString(), productSearchUnitEntry.getUnitOriginalPrice());
        assertEquals(BASE_OPTION_1_VARIANT_OPTION_PRICE_DATA.toString(), productSearchUnitEntry.getUnitPrice());
        assertEquals(STOCK_COUNT, productSearchUnitEntry.getUnitQuantity());
        assertEquals(BUSINESS_STORE_URL + BASE_OPTION_1_VARIANT_OPTION_URL, productSearchUnitEntry.getUnitUrl());
        assertEquals(1, productSearchUnitEntry.getUnitOptions().size());
        final var productSearchUnitOptionEntry = productSearchUnitEntry.getUnitOptions().get(0);
        assertEquals(BASE_OPTION_1_VARIANT_OPTION_QUALIFIER, productSearchUnitOptionEntry.getName());
        assertEquals(BASE_OPTION_1_VARIANT_OPTION_QUALIFIER_VALUE, productSearchUnitOptionEntry.getValue());
    }

    private void assertProductDataSimpleFields(ProductSearchEntry productEntry, int expectedListsSize) {
        assertNotNull(productEntry);
        assertEquals(PRODUCT_CODE, productEntry.getProductExtId());
        assertEquals(CURRENCY, productEntry.getProductCurrency());
        assertEquals(BUSINESS_STORE_URL + PRODUCT_URL, productEntry.getProductHandle());
        assertEquals(BUSINESS_STORE_NAME, productEntry.getBusinessStoreName());
        assertEquals(BUSINESS_STORE_ID, productEntry.getBusinessStoreId());
        assertEquals(BUSINESS_STORE_UID, productEntry.getBusinessStoreUid());
        assertEquals(PRODUCT_NAME, productEntry.getProductName());
        assertEquals(PRODUCT_DESCRIPTION, productEntry.getProductDescription());
        assertEquals(expectedListsSize, productEntry.getProductOptions().size());
        assertEquals(expectedListsSize, productEntry.getProductImages().size());
        assertEquals(expectedListsSize, productEntry.getProductUnits().size());
    }

    private void mockProductFacadeBehaviour() {
        final var productFacadeMockProductData = mock(ProductData.class);
        final var mockStockData = mock(StockData.class);
        when(mockStockData.getStockLevel()).thenReturn(STOCK_COUNT);
        when(productFacadeMockProductData.getStock()).thenReturn(mockStockData);
        when(productFacade.getProductForCodeAndOptions(anyString(), anyCollection())).thenReturn(productFacadeMockProductData);
    }

    private ProductSearchPageData<SearchStateData, ProductData> createMockProductSearchData() {
        final var productSearchPageData = mock(ProductSearchPageData.class);
        final var paginationData = mock(PaginationData.class);
        when(paginationData.getPageSize()).thenReturn(PAGE_SIZE);
        when(paginationData.getCurrentPage()).thenReturn(CURRENT_PAGE - 1);
        when(paginationData.getTotalNumberOfResults()).thenReturn(TOTAL_NUMBER_OF_RESULTS);
        when(paginationData.getNumberOfPages()).thenReturn(NUMBER_OF_PAGES);
        when(productSearchPageData.getPagination()).thenReturn(paginationData);
        final var mockProductData = createMockProductData(true, false);
        when(productSearchPageData.getResults()).thenReturn(List.of(mockProductData));
        return productSearchPageData;
    }

    private BusinessStore createMockBusinessStore() {
        final var businessStore = mock(BusinessStore.class);
        when(businessStore.url()).thenReturn(BUSINESS_STORE_URL);
        when(businessStore.id()).thenReturn(BUSINESS_STORE_ID);
        when(businessStore.name()).thenReturn(BUSINESS_STORE_NAME);
        when(businessStore.uid()).thenReturn(BUSINESS_STORE_UID);
        return businessStore;
    }

    private ProductData createMockProductData(boolean hasVariantOptions, boolean hasBaseOptions) {
        final var productData = mock(ProductData.class);
        final var priceData = mock(PriceData.class);
        final var productImageData = mock(ImageData.class);
        when(priceData.getCurrencyIso()).thenReturn(CURRENCY);
        when(productData.getPrice()).thenReturn(priceData);
        when(productData.getCode()).thenReturn(PRODUCT_CODE);
        when(productData.getUrl()).thenReturn(PRODUCT_URL);
        when(productData.getName()).thenReturn(PRODUCT_NAME);
        when(productData.getDescription()).thenReturn(PRODUCT_DESCRIPTION);
        when(productData.getImages()).thenReturn(List.of(productImageData));
        when(productImageData.getImageType()).thenReturn(PRIMARY);
        when((productImageData.getAltText())).thenReturn(PRODUCT_IMAGE_DATA_ALT_TEXT);
        when(productImageData.getUrl()).thenReturn(PRODUCT_IMAGE_DATA_URL);

        if (hasVariantOptions) {
            final var variantOptionData = mock(VariantOptionData.class);
            final var variantOptionQualifierData = mock(VariantOptionQualifierData.class);
            final var variantOptionDataPriceData = mock(PriceData.class);
            final var variantOptionDataImage = mock(ImageData.class);
            when(variantOptionDataPriceData.getValue()).thenReturn(VARIANT_OPTION_DATA_PRICE_DATA_VALUE);
            when(variantOptionDataImage.getUrl()).thenReturn(VARIANT_OPTION_DATA_IMAGE_URL);
            when(variantOptionDataImage.getImageType()).thenReturn(PRIMARY);
            when(variantOptionDataImage.getAltText()).thenReturn(VARIANT_OPTION_DATA_IMAGE_ALT_TEXT);
            when(variantOptionQualifierData.getImage()).thenReturn(variantOptionDataImage);
            when(variantOptionQualifierData.getQualifier()).thenReturn(VARIANT_OPTION_QUALIFIER);
            when(variantOptionQualifierData.getValue()).thenReturn(VARIANT_OPTION_QUALIFIER_VALUE);
            when(variantOptionData.getVariantOptionQualifiers()).thenReturn(List.of(variantOptionQualifierData));
            when(variantOptionData.getCode()).thenReturn(VARIANT_OPTION_DATA_CODE);
            when(variantOptionData.getPriceData()).thenReturn(variantOptionDataPriceData);
            when(variantOptionData.getUrl()).thenReturn(VARIANT_OPTION_DATA_URL);

            when(productData.getVariantOptions()).thenReturn(List.of(variantOptionData));
            when(productData.getBaseOptions()).thenReturn(null);
        } else {
            if (hasBaseOptions) {
                final var baseOptionData1 = mock(BaseOptionData.class);
                final var baseOption1VariantOption = mock(VariantOptionData.class);
                final var baseOption1VariantOptionQualifier = mock(VariantOptionQualifierData.class);
                final var baseOption1VariantOptionPriceData = mock(PriceData.class);
                when(baseOption1VariantOptionPriceData.getValue()).thenReturn(BASE_OPTION_1_VARIANT_OPTION_PRICE_DATA);
                when(baseOption1VariantOptionQualifier.getImage()).thenReturn(null);
                when(baseOption1VariantOptionQualifier.getQualifier()).thenReturn(BASE_OPTION_1_VARIANT_OPTION_QUALIFIER);
                when(baseOption1VariantOptionQualifier.getValue()).thenReturn(BASE_OPTION_1_VARIANT_OPTION_QUALIFIER_VALUE);
                when(baseOption1VariantOption.getVariantOptionQualifiers()).thenReturn(List.of(baseOption1VariantOptionQualifier));
                when(baseOption1VariantOption.getCode()).thenReturn(BASE_OPTION_1_CODE);
                when(baseOption1VariantOption.getPriceData()).thenReturn(baseOption1VariantOptionPriceData);
                when(baseOption1VariantOption.getUrl()).thenReturn(BASE_OPTION_1_VARIANT_OPTION_URL);
                when(baseOptionData1.getOptions()).thenReturn(List.of(baseOption1VariantOption));

                final var baseOptionData2 = mock(BaseOptionData.class);
                final var baseOption2VariantOption = mock(VariantOptionData.class);
                final var baseOption2VariantOptionQualifier = mock(VariantOptionQualifierData.class);
                final var baseOption2VariantOptionPriceData = mock(PriceData.class);
                final var baseOption2VariantOptionQualifierImage = mock(ImageData.class);
                when(baseOption2VariantOptionPriceData.getValue()).thenReturn(BASE_OPTION_2_VARIANT_OPTION_PRICE_DATA);
                when(baseOption2VariantOptionQualifierImage.getImageType()).thenReturn(PRIMARY);
                when(baseOption2VariantOptionQualifierImage.getAltText()).thenReturn(BASE_OPTION_2_VARIANT_OPTION_QUALIFIER_IMAGE_ALT_TEXT);
                when(baseOption2VariantOptionQualifierImage.getUrl()).thenReturn(BASE_OPTION_2_VARIANT_OPTION_QUALIFIER_IMAGE_URL);
                when(baseOption2VariantOptionQualifier.getImage()).thenReturn(baseOption2VariantOptionQualifierImage);
                when(baseOption2VariantOptionQualifier.getQualifier()).thenReturn(BASE_OPTION_2_VARIANT_OPTION_QUALIFIER);
                when(baseOption2VariantOptionQualifier.getValue()).thenReturn(BASE_OPTION_2_VARIANT_OPTION_QUALIFIER_VALUE);
                when(baseOption2VariantOption.getVariantOptionQualifiers()).thenReturn(List.of(baseOption2VariantOptionQualifier));
                when(baseOption2VariantOption.getCode()).thenReturn(BASE_OPTION_2_CODE);
                when(baseOption2VariantOption.getPriceData()).thenReturn(baseOption2VariantOptionPriceData);
                when(baseOption2VariantOption.getUrl()).thenReturn(BASE_OPTION_2_VARIANT_OPTION_URL);
                when(baseOptionData2.getOptions()).thenReturn(List.of(baseOption2VariantOption));

                when(productData.getBaseOptions()).thenReturn(List.of(baseOptionData1, baseOptionData2));
                when(productData.getVariantOptions()).thenReturn(null);
            } else {
                when(productData.getBaseOptions()).thenReturn(null);
                when(productData.getVariantOptions()).thenReturn(null);
            }
        }
        return productData;
    }

}
