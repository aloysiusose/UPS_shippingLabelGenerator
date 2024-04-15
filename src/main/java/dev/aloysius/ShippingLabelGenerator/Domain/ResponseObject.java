package dev.aloysius.ShippingLabelGenerator.Domain;

import java.util.List;

public class ResponseObject {
 private final ShipmentResponse response;

 public ResponseObject(ShipmentResponse response) {
  this.response = response;
 }
}
record ShipmentResponse(Response response, ShipmentResults shipmentResults) {
}

record Response(ResponseStatus responseStatus, List<Alert> alert, TransactionReference transactionReference) {
}
record ResponseStatus(String code, String description) {
}

record Alert(String code, String description) {
}

record TransactionReference(String customerContext) {
}

record ShipmentResults(List<Disclaimer> disclaimer, ShipmentCharges shipmentCharges, NegotiatedRateCharges negotiatedRateCharges, FRSShipmentData fRSShipmentData, String ratingMethod, String billableWeightCalculationMethod, String billingWeight, String shipmentIdentificationNumber, String mIDualReturnShipmentKey, String barCodeImage, List<PackageResults> packageResults, List<ControlLogReceipt> controlLogReceipt, Form form, String cODTurnInPage, String highValueReport, String labelURL, String localLanguageLabelURL, String receiptURL, String localLanguageReceiptURL, List<String> dGPaperImage, String masterCartonID, String roarRatedIndicator) {
}
record Disclaimer(String code, String description) {
}

record ShipmentCharges(String rateChart, BaseServiceCharge baseServiceCharge, TransportationCharges transportationCharges, List<ItemizedCharges> itemizedCharges, ServiceOptionsCharges serviceOptionsCharges, List<TaxCharges> taxCharges, TotalCharges totalCharges, TotalChargesWithTaxes totalChargesWithTaxes) {
}
record NegotiatedRateCharges(List<ItemizedCharges> itemizedCharges, List<TaxCharges> taxCharges, TotalCharge totalCharge, TotalChargesWithTaxes totalChargesWithTaxes) {
}

record FRSShipmentData(TransportationCharges transportationCharges, FreightDensityRate freightDensityRate, List<HandlingUnits> handlingUnits) {
}

record TransportationCharges(GrossCharge grossCharge, DiscountAmount discountAmount, DiscountPercentage discountPercentage, NetCharge netCharge) {
}

record FreightDensityRate(String density, String totalCubicFeet) {
}

record HandlingUnits(String quantity, Type type, Dimensions dimensions, AdjustedHeight adjustedHeight) {
}

record Type(String code, String description) {
}
record Dimensions(UnitOfMeasurement unitOfMeasurement, String length, String width, String height) {
}

record UnitOfMeasurement(String code, String description) {
}
record AdjustedHeight(String value, UnitOfMeasurement unitOfMeasurement) {
}
record GrossCharge(String currencyCode, String monetaryValue) {
}
record DiscountAmount(String currencyCode, String monetaryValue) {
}record DiscountPercentage(String value) {
}
record NetCharge(String currencyCode, String monetaryValue) {
}record ItemizedCharges(String code, String description, String currencyCode, String monetaryValue, String subType) {
}

record TaxCharges(String type, String monetaryValue) {
}
record TotalCharges(String currencyCode, String monetaryValue) {
}
record TotalChargesWithTaxes(String currencyCode, String monetaryValue) {
}
record TotalCharge(String currencyCode, String monetaryValue) {
}
record PackageResults(String trackingNumber, RateModifier rateModifier, BaseServiceCharge baseServiceCharge, ServiceOptionsCharges serviceOptionsCharges, ShippingLabel shippingLabel, ShippingReceipt shippingReceipt, USPSPICNumber USPSPICNumber, CN22Number CN22Number, List<Accessorial> accessorial, SimpleRate simpleRate, Form form, List<ItemizedCharges> itemizedCharges, NegotiatedCharges negotiatedCharges) {
}

record RateModifier(ModifierType modifierType, String modifierDesc, String amount) {
}

record ModifierType(String code, String description) {
}
record BaseServiceCharge(String currencyCode, String monetaryValue) {
}

record ServiceOptionsCharges(String currencyCode, String monetaryValue) {
}

record ShippingLabel(ImageFormat imageFormat, String graphicImage, List<String> graphicImagePart, String internationalSignatureGraphicImage, String htmlImage, String pdf417) {
}

 record ImageFormat(String code, String description) {
}
record ShippingReceipt(ImageFormat imageFormat, String graphicImage) {
}

record USPSPICNumber(String value) {
}

record CN22Number(String value) {
}
record Accessorial(String code, String description) {
}

record SimpleRate(String code) {
}

record Form(String code, String description, Image image, String formGroupId, String formGroupIdName) {
}

record Image(ImageFormat imageFormat, String graphicImage) {
}

record ControlLogReceipt(ImageFormat imageFormat, String graphicImage) {}
record NegotiatedCharges(List<ItemizedCharges> itemizedCharges) {
}
