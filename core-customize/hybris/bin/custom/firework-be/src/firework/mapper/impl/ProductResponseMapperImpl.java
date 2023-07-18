package firework.mapper.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commercefacades.product.data.VariantOptionQualifierData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import firework.mapper.ProductResponseMapper;
import firework.model.businessstore.get.BusinessStore;
import firework.model.product.get.ProductSearchEntry;
import firework.model.product.get.ProductSearchImageEntry;
import firework.model.product.get.ProductSearchUnitEntry;
import firework.model.product.get.ProductSearchUnitOptionEntry;
import firework.model.product.get.ProductsSearchData;
import firework.service.BusinessStoreService;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import static de.hybris.platform.commercefacades.product.data.ImageDataType.PRIMARY;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
public class ProductResponseMapperImpl implements ProductResponseMapper {

    private static final EnumSet<ProductOption> PRODUCT_OPTIONS_SET = EnumSet.allOf(ProductOption.class);
    private static final String ROLLUP_PROPERTY = "rollupProperty";

    private final ProductFacade productFacade;

    private final BusinessStoreService businessStoreService;

    @Override
    public ProductsSearchData createProductSearchData(String businessStoreId, ProductSearchPageData<SearchStateData, ProductData> searchResult) {
        return ProductsSearchData.builder()
                .pageSize(searchResult.getPagination().getPageSize())
                .pageNumber(searchResult.getPagination().getCurrentPage() + 1)
                .totalEntries(searchResult.getPagination().getTotalNumberOfResults())
                .totalPages(searchResult.getPagination().getNumberOfPages())
                .entries(createEntries(searchResult, businessStoreId))
                .build();
    }

    private List<ProductSearchEntry> createEntries(ProductSearchPageData<SearchStateData, ProductData> searchResult, String businessStoreId) {
        final var fireworkBusinessStore = businessStoreService.getBusinessStoreByStoreId(businessStoreId);
        return searchResult.getResults()
                .stream()
                .map((ProductData productData) -> createProductEntry(productData, fireworkBusinessStore))
                .toList();
    }

    @Override
    public ProductSearchEntry createProductEntry(ProductData productData, BusinessStore fireworkBusinessStore) {
        final var price = productData.getPrice();
        final var variantOptions = getVariantOptions(productData);
        final var includeImageOptions = includeImageOptions(variantOptions);
        return ProductSearchEntry.builder()
                .productExtId(productData.getCode())
                .productCurrency(price.getCurrencyIso())
                .productHandle(fireworkBusinessStore.url() + productData.getUrl())
                .businessStoreId(fireworkBusinessStore.id())
                .businessStoreName(fireworkBusinessStore.name())
                .businessStoreUid(fireworkBusinessStore.uid())
                .productName(productData.getName())
                .productDescription(productData.getDescription())
                .productOptions(createProductOptions(variantOptions, includeImageOptions))
                .productImages(createProductImages(variantOptions, productData.getImages(), fireworkBusinessStore.url(), includeImageOptions))
                .productUnits(createProductUnits(variantOptions, fireworkBusinessStore.url(), includeImageOptions))
                .build();
    }

    private boolean includeImageOptions(List<VariantOptionData> variantOptions) {
        return variantOptions.stream()
                .flatMap(variantOptionData -> variantOptionData.getVariantOptionQualifiers().stream())
                .map(VariantOptionQualifierData::getImage)
                .allMatch(Objects::nonNull);
    }

    private Set<String> createProductOptions(List<VariantOptionData> variantOptions, boolean includeImageOptions) {
        return variantOptions.stream()
                .flatMap(variantOptionData -> variantOptionData.getVariantOptionQualifiers().stream())
                .filter(variantOptionQualifierData -> includeImageOptions || isNull(variantOptionQualifierData.getImage()))
                .map(VariantOptionQualifierData::getQualifier)
                .filter(qualifier -> !ROLLUP_PROPERTY.equalsIgnoreCase(qualifier))
                .collect(toSet());
    }

    private List<ProductSearchImageEntry> createProductImages(List<VariantOptionData> variantOptions, Collection<ImageData> images, String siteRootUrl, boolean includeImageOptions) {
        final var counter = new AtomicInteger(0);
        if (isNotEmpty(variantOptions)) {
            final var productSearchImageEntries = getProductSearchImageEntries(variantOptions, siteRootUrl, includeImageOptions, counter);
            if (productSearchImageEntries.isEmpty()) {
                return getProductSearchImageEntries(variantOptions, images, siteRootUrl, includeImageOptions, counter);
            } else {
                return productSearchImageEntries;
            }
        }
        return emptyList();
    }

    private List<ProductSearchImageEntry> getProductSearchImageEntries(List<VariantOptionData> variantOptions, String siteRootUrl, boolean includeImageOptions, AtomicInteger counter) {
        return variantOptions.stream()
                .map(variantOptionData -> createProductImage(variantOptionData, siteRootUrl, includeImageOptions))
                .filter(Objects::nonNull)
                .sorted(comparing(entry -> entry.getUnitIdentifiers().get(0)))
                .peek(entry -> entry.setImagePosition(counter.getAndIncrement()))
                .toList();
    }

    private ProductSearchImageEntry createProductImage(VariantOptionData variantOptionData, String siteRootUrl, boolean includeImageOptions) {
        final var unitName = createUnitName(variantOptionData, includeImageOptions);
        return variantOptionData.getVariantOptionQualifiers().stream()
                .map(VariantOptionQualifierData::getImage)
                .filter(Objects::nonNull)
                .filter(imageData -> PRIMARY.equals(imageData.getImageType()))
                .findFirst()
                .map(imageData -> {
                    final var pattern = Pattern.compile("/medias/\\?context=(.+)");
                    final var matcher = pattern.matcher(imageData.getUrl());
                    final var imageId = matcher.find() ?
                            matcher.group(1) :
                            EMPTY;
                    return ProductSearchImageEntry.builder()
                            .imageExtId(imageId)
                            .imageSrc(siteRootUrl + imageData.getUrl())
                            .unitIdentifiers(List.of(variantOptionData.getCode()))
                            .unitNames(List.of(unitName))
                            .build();
                })
                .orElse(null);

    }

    private List<ProductSearchImageEntry> getProductSearchImageEntries(List<VariantOptionData> variantOptions, Collection<ImageData> images, String siteRootUrl, boolean includeImageOptions, AtomicInteger counter) {
        final var unitIdentifiers = getUnitIdentifiers(variantOptions);
        final var unitNames = getUnitNames(variantOptions, includeImageOptions);
        return getProductSearchImageEntries(images, siteRootUrl, counter, unitIdentifiers, unitNames);
    }

    private List<ProductSearchUnitEntry> createProductUnits(List<VariantOptionData> variantOptions, String siteRootUrl, boolean includeImageOptions) {
        final var counter = new AtomicInteger(0);
        return variantOptions.stream()
                .map(variantOptionData -> createProductUnit(variantOptionData, siteRootUrl, includeImageOptions))
                .sorted(comparing(ProductSearchUnitEntry::getUnitName))
                .peek(entry -> entry.setUnitPosition(counter.getAndIncrement()))
                .toList();
    }

    private List<String> getUnitIdentifiers(List<VariantOptionData> variantOptions) {
        return variantOptions.stream()
                .map(VariantOptionData::getCode)
                .toList();
    }

    private List<String> getUnitNames(List<VariantOptionData> variantOptions, boolean includeImageOptions) {
        return variantOptions.stream()
                .map(variantOptionData -> createUnitName(variantOptionData, includeImageOptions))
                .distinct()
                .toList();
    }

    private List<ProductSearchImageEntry> getProductSearchImageEntries(Collection<ImageData> images, String siteRootUrl, AtomicInteger counter, List<String> unitIdentifiers, List<String> unitNames) {
        return images.stream()
                .filter(imageData -> PRIMARY.equals(imageData.getImageType()))
                .map(imageData -> createProductImage(imageData, unitNames, unitIdentifiers, siteRootUrl))
                .peek(entry -> entry.setImagePosition(counter.getAndIncrement()))
                .toList();
    }

    private ProductSearchImageEntry createProductImage(ImageData imageData, List<String> unitNames, List<String> unitIdentifiers, String siteRootUrl) {
        final var pattern = Pattern.compile("/medias/\\?context=(.+)");
        final var matcher = pattern.matcher(imageData.getUrl());
        final var imageId = matcher.find() ?
                matcher.group(1) :
                EMPTY;
        return ProductSearchImageEntry.builder()
                .imageExtId(imageId)
                .imageSrc(siteRootUrl + imageData.getUrl())
                .unitNames(unitNames)
                .unitIdentifiers(unitIdentifiers)
                .build();
    }

    private List<VariantOptionData> getVariantOptions(ProductData productData) {
        return Optional.ofNullable(productData.getVariantOptions())
                .orElseGet(() -> getOptionDataFromBaseOptions(productData));
    }

    private List<VariantOptionData> getOptionDataFromBaseOptions(ProductData productData) {
        final var baseOptions = productData.getBaseOptions();
        if (isEmpty(baseOptions)) {
            return emptyList();
        } else {
            final var variantOptionsData = baseOptions.stream()
                    .flatMap(baseOptionData -> baseOptionData.getOptions().stream())
                    .collect(toList());
            final var hasVariantOptionsWithImages = variantOptionsData.stream()
                    .flatMap(variantOptionData -> variantOptionData.getVariantOptionQualifiers().stream())
                    .anyMatch(variantOptionQualifierData -> nonNull(variantOptionQualifierData.getImage()));
            if (hasVariantOptionsWithImages) {
                variantOptionsData.removeIf(variantOptionData ->
                        variantOptionData.getVariantOptionQualifiers().stream().allMatch(variantOptionQualifierData -> nonNull(variantOptionQualifierData.getImage())));
            }
            return variantOptionsData;
        }
    }

    private ProductSearchUnitEntry createProductUnit(VariantOptionData variantOptionData, String siteRootUrl, boolean includeImageOptions) {
        final var unitName = createUnitName(variantOptionData, includeImageOptions);
        final var unitOptions = createUnitOptions(variantOptionData, includeImageOptions);
        final var product = productFacade.getProductForCodeAndOptions(variantOptionData.getCode(), PRODUCT_OPTIONS_SET);
        final var unitPrice = variantOptionData.getPriceData().getValue().toString();
        return ProductSearchUnitEntry.builder()
                .unitExtId(variantOptionData.getCode())
                .unitName(unitName)
                .unitOptions(unitOptions)
                .unitOriginalPrice(unitPrice)
                .unitPrice(unitPrice)
                .unitQuantity(product.getStock().getStockLevel())
                .unitUrl(siteRootUrl + variantOptionData.getUrl())
                .build();
    }

    private String createUnitName(VariantOptionData variantOptionData, boolean includeImageOptions) {
        return variantOptionData.getVariantOptionQualifiers().stream()
                .filter(variantOptionQualifierData -> includeImageOptions || isNull(variantOptionQualifierData.getImage()))
                .filter(variantOptionQualifierData -> !ROLLUP_PROPERTY.equalsIgnoreCase(variantOptionQualifierData.getQualifier()))
                .map(VariantOptionQualifierData::getValue)
                .filter(Objects::nonNull)
                .distinct()
                .collect(joining(" / "));
    }

    private List<ProductSearchUnitOptionEntry> createUnitOptions(VariantOptionData variantOptionData, boolean includeImageOptions) {
        return variantOptionData.getVariantOptionQualifiers().stream()
                .filter(variantOptionQualifierData -> includeImageOptions || isNull(variantOptionQualifierData.getImage()))
                .filter(variantOptionQualifierData -> !ROLLUP_PROPERTY.equalsIgnoreCase(variantOptionQualifierData.getQualifier()))
                .collect(toMap(VariantOptionQualifierData::getQualifier, VariantOptionQualifierData::getValue, (first, second) -> first))
                .entrySet().stream()
                .map(this::createUnitOption)
                .toList();
    }

    private ProductSearchUnitOptionEntry createUnitOption(Map.Entry<String, String> unitOptionMapEntry) {
        return ProductSearchUnitOptionEntry.builder()
                .name(unitOptionMapEntry.getKey())
                .value(unitOptionMapEntry.getValue())
                .build();
    }
}
