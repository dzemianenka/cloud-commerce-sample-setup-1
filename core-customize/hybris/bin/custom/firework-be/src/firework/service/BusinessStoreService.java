package firework.service;

import firework.model.businessstore.create.CreateBusinessStore;
import firework.model.businessstore.create.InputCreateBusinessStore;
import firework.model.businessstore.get.BusinessStore;
import firework.model.businessstore.update.InputUpdateBusinessStore;
import firework.model.businessstore.update.UpdateBusinessStore;

import java.util.List;

public interface BusinessStoreService {

    List<BusinessStore> getBusinessStores();

    BusinessStore getBusinessStoreByStoreId(String businessStoreId);

    CreateBusinessStore createBusinessStore(InputCreateBusinessStore input);

    UpdateBusinessStore updateBusinessStore(InputUpdateBusinessStore input);

    void syncBusinessStores();
}
