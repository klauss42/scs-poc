package com.demo.scs.poc.vehicle.service1.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CarResource extends ResourceWithEmbeddeds {

   private Long carId;
   private String vin;
   private String type;
   private String model;
   private String shortModel;

   @JsonCreator
   public CarResource(@JsonProperty("carId") Long carId,
                      @JsonProperty("vin") String vin,
                      @JsonProperty("type") String type,
                      @JsonProperty("model") String model,
                      @JsonProperty("shortModel") String shortModel) {
      this.carId = carId;
      this.vin = vin;
      this.type = type;
      this.model = model;
      this.shortModel = shortModel;
   }

    public Long getCarId() {
        return carId;
    }

    public String getVin() {
      return vin;
   }

   public String getType() {
      return type;
   }

   public String getModel() {
      return model;
   }

    public String getShortModel() {
        return shortModel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CarResource{");
        sb.append("carId=").append(carId);
        sb.append(", vin='").append(vin).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", shortModel='").append(shortModel).append('\'');
        sb.append('}');
        sb.append(", ").append(super.toString());
        return sb.toString();
    }
}
