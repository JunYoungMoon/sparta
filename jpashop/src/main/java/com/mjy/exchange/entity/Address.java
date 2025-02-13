package com.mjy.exchange.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String zipCode;
    private String street;

    @Builder
    public Address(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }

    //임베디드 값 타입을 사용하면 이렇게 추가 메서드를 작성하고 공통된 컬럼을 관리 할수 있다.
    public String fullAddress(){
        return getCity() + " " + getStreet() + " " + getZipCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getZipCode(), address.getZipCode()) && Objects.equals(getStreet(), address.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getZipCode(), getStreet());
    }
}
