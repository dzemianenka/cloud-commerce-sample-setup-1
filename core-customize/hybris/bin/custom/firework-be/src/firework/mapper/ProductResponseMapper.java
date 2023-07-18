package firework.mapper;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import firework.model.businessstore.get.BusinessStore;
import firework.model.product.get.ProductSearchEntry;
import firework.model.product.get.ProductsSearchData;

public interface ProductResponseMapper {

    ProductsSearchData createProductSearchData(String businessStoreId, ProductSearchPageData<SearchStateData, ProductData> searchResult);

    ProductSearchEntry createProductEntry(ProductData productData, BusinessStore fireworkBusinessStore);
}
