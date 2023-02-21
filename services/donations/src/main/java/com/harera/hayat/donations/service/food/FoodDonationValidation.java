package com.harera.hayat.donations.service.food;

import com.harera.hayat.donations.model.food.FoodDonationDto;
import com.harera.hayat.donations.model.food.FoodDonationUpdateRequest;
import com.harera.hayat.donations.service.DonationValidation;
import com.harera.hayat.framework.exception.FieldFormatException;
import com.harera.hayat.framework.exception.MandatoryFieldException;
import com.harera.hayat.framework.util.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class FoodDonationValidation extends DonationValidation {

    public void validateCreate(FoodDonationDto foodDonationRequest) {
        validateDonation(foodDonationRequest);
        validateMandatory(foodDonationRequest);
        validateFormat(foodDonationRequest);
    }

    private void validateFormat(FoodDonationDto foodDonationRequest) {
        if (foodDonationRequest.getAmount() < 0
                        || foodDonationRequest.getAmount() > 10000) {
            throw new FieldFormatException(ErrorCode.FORMAT_FOOD_DONATION_AMOUNT,
                            "amount",
                            foodDonationRequest.getAmount().toString());
        }
    }

    private void validateMandatory(FoodDonationDto foodDonationRequest) {
        if (foodDonationRequest.getAmount() == null) {
            throw new MandatoryFieldException(ErrorCode.MANDATORY_FOOD_DONATION_AMOUNT,
                            "amount");
        }
        if (foodDonationRequest.getUnitId() == null) {
            throw new MandatoryFieldException(ErrorCode.MANDATORY_FOOD_DONATION_UNIT,
                            "unit");
        }
        if (foodDonationRequest.getFoodExpirationDate() == null) {
            throw new MandatoryFieldException(
                            ErrorCode.MANDATORY_FOOD_DONATION_FOOD_EXPIRATION_DATE,
                            "expiration_date");
        }
    }

    public void validateUpdate(Long id, FoodDonationUpdateRequest request) {
        validateDonation(request);
        validateMandatory(request);
        validateFormat(request);
    }
}
