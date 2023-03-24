package com.harera.hayat.needs.service.medicine;

import com.harera.hayat.framework.exception.DocumentNotFoundException;
import com.harera.hayat.framework.repository.repository.MedicineRepository;
import com.harera.hayat.framework.repository.repository.MedicineUnitRepository;
import com.harera.hayat.framework.service.city.CityService;
import com.harera.hayat.framework.service.file.CloudFileService;
import com.harera.hayat.framework.service.medicine.MedicineService;
import com.harera.hayat.framework.service.medicine.MedicineUnitService;
import com.harera.hayat.needs.model.NeedCategory;
import com.harera.hayat.needs.model.NeedStatus;
import com.harera.hayat.needs.model.medicine.MedicineNeed;
import com.harera.hayat.needs.model.medicine.MedicineNeedRequest;
import com.harera.hayat.needs.model.medicine.MedicineNeedResponse;
import com.harera.hayat.needs.model.medicine.MedicineNeedUpdateRequest;
import com.harera.hayat.needs.repository.medicine.MedicineNeedRepository;
import com.harera.hayat.needs.service.BaseService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.harera.hayat.framework.util.FileUtils.convertMultiPartToFile;

@Service
public class MedicineNeedService implements BaseService {

    private final MedicineNeedValidation needValidation;
    private final CityService cityService;
    private final MedicineUnitService medicineUnitService;
    private final MedicineService medicineService;
    private final MedicineUnitRepository medicineUnitRepository;
    private final ModelMapper modelMapper;
    private final MedicineNeedRepository medicineNeedRepository;
    private final MedicineRepository medicineRepository;
    private final CloudFileService cloudFileService;

    public MedicineNeedService(MedicineNeedValidation needValidation,
                    CityService cityService, MedicineUnitService medicineUnitService,
                    MedicineService medicineService,
                    MedicineUnitRepository medicineUnitRepository,
                    ModelMapper modelMapper,
                    MedicineNeedRepository medicineNeedRepository,
                    MedicineRepository medicineRepository,
                    CloudFileService cloudFileService) {
        this.needValidation = needValidation;
        this.cityService = cityService;
        this.medicineUnitService = medicineUnitService;
        this.medicineService = medicineService;
        this.medicineUnitRepository = medicineUnitRepository;
        this.modelMapper = modelMapper;
        this.medicineNeedRepository = medicineNeedRepository;
        this.medicineRepository = medicineRepository;
        this.cloudFileService = cloudFileService;
    }

    public MedicineNeedResponse create(MedicineNeedRequest medicineNeedRequest) {
        needValidation.validateCreate(medicineNeedRequest);

        MedicineNeed medicineNeed =
                        modelMapper.map(medicineNeedRequest, MedicineNeed.class);
        medicineNeed.setCategory(NeedCategory.MEDICINE);
        medicineNeed.setCity(cityService.get(medicineNeedRequest.getCityId()));
        // TODO: 28/02/23 get user from token
        medicineNeed.setMedicineUnit(
                        medicineUnitService.get(medicineNeedRequest.getMedicineUnitId()));
        medicineNeed.setMedicine(
                        medicineService.get(medicineNeedRequest.getMedicineId()));

        medicineNeed.setNeedDate(LocalDateTime.now());
        medicineNeed.setNeedExpirationDate(LocalDateTime.now().plusDays(45));

        medicineNeedRepository.save(medicineNeed);
        return modelMapper.map(medicineNeed, MedicineNeedResponse.class);
    }

    public MedicineNeedResponse get(String id) {
        return modelMapper.map(
                        medicineNeedRepository.findById(id)
                                        .orElseThrow(() -> new DocumentNotFoundException(
                                                        MedicineNeed.class, id)),
                        MedicineNeedResponse.class);
    }

    public List<MedicineNeedResponse> list(int size, int page) {
        return medicineNeedRepository.findAll(Pageable.ofSize(size).withPage(page))
                        .map(medicineNeed -> modelMapper.map(medicineNeed,
                                        MedicineNeedResponse.class))
                        .toList();
    }

    public MedicineNeedResponse update(String id, MedicineNeedUpdateRequest request) {
        needValidation.validateUpdate(id, request);

        MedicineNeed medicineNeed = medicineNeedRepository.findById(id).orElseThrow(
                        () -> new DocumentNotFoundException(MedicineNeed.class, id));
        modelMapper.map(request, medicineNeed);

        medicineNeed.setCategory(NeedCategory.MEDICINE);
        medicineNeed.setStatus(NeedStatus.PENDING);
        medicineNeed.setCity(cityService.get(request.getCityId()));
        medicineNeed.setMedicineUnit(
                        medicineUnitService.get(request.getMedicineUnitId()));
        medicineNeed.setMedicine(medicineService.get(request.getMedicineId()));

        // TODO: 28/02/23 get user from token
        // TODO: 28/02/23 send request to ai model

        medicineNeedRepository.save(medicineNeed);
        return modelMapper.map(medicineNeed, MedicineNeedResponse.class);

    }

    public List<MedicineNeedResponse> search(String query, int page) {
        page = Integer.max(page, 1) - 1;
        return medicineNeedRepository.searchByTitleRegexOrDescriptionRegex(query, query).stream()
                        .map(medicineNeed -> modelMapper.map(medicineNeed,
                                        MedicineNeedResponse.class))
                        .toList();
    }

    public MedicineNeedResponse updateImage(String id, MultipartFile file) {
        MedicineNeed medicineNeed = medicineNeedRepository.findById(id).orElseThrow(
                        () -> new DocumentNotFoundException(MedicineNeed.class, id));

        String imageUrl = cloudFileService.uploadFile(convertMultiPartToFile(file));
        if (medicineNeed.getImageUrl() == null) {
            medicineNeed.setImageUrl(imageUrl);
        } else {
            cloudFileService.deleteFile(medicineNeed.getImageUrl());
            medicineNeed.setImageUrl(imageUrl);
        }

        medicineNeedRepository.save(medicineNeed);
        return modelMapper.map(medicineNeed, MedicineNeedResponse.class);
    }
}
