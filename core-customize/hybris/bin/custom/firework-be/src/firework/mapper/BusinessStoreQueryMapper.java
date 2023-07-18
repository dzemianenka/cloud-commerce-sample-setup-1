package firework.mapper;

import firework.model.businessstore.create.InputCreateBusinessStore;
import firework.model.businessstore.update.InputUpdateBusinessStore;

public interface BusinessStoreQueryMapper {

    String mapToCreateBusinessStoreQuery(InputCreateBusinessStore input);

    String mapToUpdateBusinessStoreQuery(InputUpdateBusinessStore input);

    String mapToGetHmacSecret(String businessStoreId);
}
