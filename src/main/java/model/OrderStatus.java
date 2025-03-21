package model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PREPARE("준비중"),
    DONE("완료");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }


}
