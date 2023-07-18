package firework.service;

public interface SecurityService {
    String getHmacSecret(String businessStoreId);
}
