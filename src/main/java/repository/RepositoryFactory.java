package repository;

public class RepositoryFactory {
    public static OrderRepository getRepository(String repositoryType) {
        switch (repositoryType.toLowerCase()) {
            case "object":
                return new ObjectOrderRepository();
            case "txt":
                return new TxtOrderRepository();
            default:
                throw new IllegalArgumentException("지정되지 않은 저장소 타입 " + repositoryType);
        }
    }
}
