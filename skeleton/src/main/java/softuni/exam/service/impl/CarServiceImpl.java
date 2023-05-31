package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarImportDto;
import softuni.exam.models.dto.wrappers.CarWrapperDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_CAR;
import static softuni.exam.constants.Messages.VALID_CAR_INPUT;
import static softuni.exam.constants.Paths.CARS_XML_PATH;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final StringBuilder sb;
    private final ValidationUtils validationUtils;
    private final XmlParser xmlParser;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ModelMapper modelMapper, StringBuilder sb, ValidationUtils validationUtils, XmlParser xmlParser) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.sb = sb;
        this.validationUtils = validationUtils;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {

        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(CARS_XML_PATH);
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        final File file = CARS_XML_PATH.toFile();

        final CarWrapperDto carWrapperDto = xmlParser.fromFile(file, CarWrapperDto.class);
        List<CarImportDto> cars = carWrapperDto.getCar();

        for (CarImportDto car : cars) {
            boolean isValid = this.validationUtils.isValid(car);

            if (this.carRepository.findFirstByPlateNumber(car.getPlateNumber()).isPresent()) {
                continue;
            }

            if (isValid) {
                Car carToSave = this.modelMapper.map(car, Car.class);
                this.carRepository.saveAndFlush(carToSave);

                sb.append(String.format(VALID_CAR_INPUT, car.getCarMake(), car.getCarModel()));
            } else {
                sb.append(INVALID_CAR).append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
