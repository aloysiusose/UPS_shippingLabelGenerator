package dev.aloysius.ShippingLabelGenerator.Domain;

import java.util.List;

public class RequestObject {
    private final ShipmentRequest shipmentRequest;

    public RequestObject(ShipmentRequest shipmentRequest) {
        this.shipmentRequest = shipmentRequest;
    }
}
record ShipmentRequest(Request request, Shipment shipment, LabelSpecification labelSpecification) {
}

record Request(String subVersion, String requestOption, TransactionReference transactionReference) {
}

//record TransactionReference(String customerContext) {
//}

record Shipment(String description, Shipper shipper, ShipTo shipTo, ShipFrom shipFrom, PaymentInformation paymentInformation, Service service, Package pack) {
}

record Shipper(String name, String attentionName, String taxIdentificationNumber, Phone phone, String shipperNumber, String faxNumber, Address address) {
}

record Phone(String number, String extension) {
}

record ShipTo(String name, String attentionName, Phone phone, Address address, String residential) {
}

 record ShipFrom(String name, String attentionName, Phone phone, FaxNumber faxNumber, Address address) {
}

 record FaxNumber(String number) {
}

record Address(List<String> addressLine, String city, String stateProvinceCode, String postalCode, String countryCode) {
}

record PaymentInformation(ShipmentCharge shipmentCharge) {
}

record ShipmentCharge(String type, BillShipper billShipper) {
}

record BillShipper(String accountNumber) {
}

record Service(String code, String description) {
}

record Package(String description, Packaging packaging, Dimensions dimensions, PackageWeight packageWeight) {
}

record Packaging(String code, String description) {
}

//record Dimensions(UnitOfMeasurement unitOfMeasurement, String length, String width, String height) {
//}

record PackageWeight(UnitOfMeasurement unitOfMeasurement, String weight) {
}

record LabelSpecification(LabelImageFormat labelImageFormat, String httpUserAgent) {
}

record LabelImageFormat(String code, String description) {
}
