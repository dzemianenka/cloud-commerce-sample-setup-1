package firework.service;

import firework.model.product.get.ProductSearchEntry;
import firework.model.product.get.ProductsSearchData;

public interface FwProductService {

    ProductsSearchData getProductsForStore(String businessId, String businessStoreId, int page, int pageSize);

    ProductSearchEntry getProductForStoreByProductExtId(String productExtId, String businessStoreId);
}
