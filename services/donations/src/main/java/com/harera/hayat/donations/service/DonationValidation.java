package com.harera.hayat.donations.service;

import com.harera.hayat.donations.model.CommunicationMethod;
import com.harera.hayat.donations.model.DonationDto;
import com.harera.hayat.framework.exception.EntityNotFoundException;
import com.harera.hayat.framework.exception.FieldFormatException;
import com.harera.hayat.framework.exception.MandatoryFieldException;
import com.harera.hayat.framework.model.city.City;
import com.harera.hayat.framework.repository.city.CityRepository;
import com.harera.hayat.framework.util.ErrorCode;
import com.harera.hayat.framework.util.FieldFormat;
import org.springframework.stereotype.Service;

import static com.harera.hayat.donations.util.CommunicationRegexUtils.isValidTelegramLink;
import static com.harera.hayat.donations.util.CommunicationRegexUtils.isValidWhatsappLink;
import static com.harera.hayat.framework.util.ErrorCode.NOT_FOUND_CITY;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public class DonationValidation {

    private final CityRepository cityRepository;

    public DonationValidation(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public void validateCreate(DonationDto donationDto) {
        validateMandatory(donationDto);
        validateFormat(donationDto);
        validateExisting(donationDto);
    }

    private void validateExisting(DonationDto donationDto) {
        if (!cityRepository.existsById(donationDto.getCityId()))
            throw new EntityNotFoundException(City.class, donationDto.getCityId(),
                            NOT_FOUND_CITY);
    }

    public void validateUpdate(DonationDto donationDto) {
        validateMandatory(donationDto);
        validateFormat(donationDto);
    }

    private void validateFormat(DonationDto donationDto) {
        if (donationDto.getTitle().length() < 4
                        || donationDto.getTitle().length() > 100) {
            throw new FieldFormatException(ErrorCode.FORMAT_DONATION_TITLE, "title",
                            FieldFormat.TITLE_PATTERN);
        }

        if (donationDto.getTelegramLink() != null
                        && !isValidTelegramLink(donationDto.getTelegramLink())) {
            throw new FieldFormatException("", "telegram_link",
                            donationDto.getTelegramLink());
        }

        if (donationDto.getWhatsappLink() != null
                        && !isValidWhatsappLink(donationDto.getWhatsappLink())) {
            throw new FieldFormatException("", "whatsapp_link",
                            donationDto.getWhatsappLink());
        }
    }

    private void validateMandatory(DonationDto donationDto) {
        if (isBlank(donationDto.getTitle())) {
            throw new MandatoryFieldException(ErrorCode.MANDATORY_DONATION_TITLE,
                            "title");
        }
        if (donationDto.getCommunicationMethod() == null) {
            throw new MandatoryFieldException(
                            ErrorCode.MANDATORY_DONATION_COMMUNICATION_METHOD,
                            "communication_method");
        }
        if (donationDto.getCityId() == null) {
            throw new MandatoryFieldException(ErrorCode.MANDATORY_DONATION_CITY_ID,
                            "city_id");
        }
        if (donationDto.getCommunicationMethod() == CommunicationMethod.CHAT
                        && isBlank(donationDto.getTelegramLink())
                        && isBlank(donationDto.getWhatsappLink())) {
            throw new MandatoryFieldException(null, "telegram_link or whatsapp_link");
        }
    }
}
