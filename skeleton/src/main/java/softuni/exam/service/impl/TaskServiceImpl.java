package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TaskImportDto;
import softuni.exam.models.dto.wrappers.TaskWrapperDto;
import softuni.exam.models.entity.Car;
import softuni.exam.models.entity.Mechanic;
import softuni.exam.models.entity.Part;
import softuni.exam.models.entity.Task;
import softuni.exam.models.enums.CarType;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.MechanicRepository;
import softuni.exam.repository.PartRepository;
import softuni.exam.repository.TaskRepository;
import softuni.exam.service.TaskService;
import softuni.exam.util.ValidationUtils;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static softuni.exam.constants.Messages.INVALID_TASK;
import static softuni.exam.constants.Messages.VALID_TASK_INPUT;
import static softuni.exam.constants.Paths.TASKS_XML_PATH;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final MechanicRepository mechanicRepository;
    private final CarRepository carRepository;
    private final PartRepository partRepository;
    private final ModelMapper modelMapper;
    private final StringBuilder sb;
    private final ValidationUtils validationUtils;
    private final XmlParser xmlParser;

    public TaskServiceImpl(TaskRepository taskRepository, MechanicRepository mechanicRepository, CarRepository carRepository, PartRepository partRepository, ModelMapper modelMapper, StringBuilder sb, ValidationUtils validationUtils, XmlParser xmlParser) {
        this.taskRepository = taskRepository;
        this.mechanicRepository = mechanicRepository;
        this.carRepository = carRepository;
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.sb = sb;
        this.validationUtils = validationUtils;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {

        return this.taskRepository.count() > 0;
    }

    @Override
    public String readTasksFileContent() throws IOException {
        return Files.readString(TASKS_XML_PATH);
    }

    @Override
    public String importTasks() throws IOException, JAXBException {
        File file = TASKS_XML_PATH.toFile();

        TaskWrapperDto taskWrapperDto = xmlParser.fromFile(file, TaskWrapperDto.class);
        List<TaskImportDto> tasks = taskWrapperDto.getTasks();

        for (TaskImportDto task : tasks) {
            boolean isValid = this.validationUtils.isValid(task);

            if (isValid) {
                if (this.mechanicRepository.findFirstByFirstName(task.getMechanic().getFirstName()).isPresent()) {
                    Task taskToSave = this.modelMapper.map(task, Task.class);

                    Car currentCar = this.carRepository.findFirstById(task.getCar().getId()).get();
                    Mechanic currentMechanic = this.mechanicRepository.findFirstByFirstName(task.getMechanic().getFirstName()).get();
                    Part currentPart = this.partRepository.findFirstById(task.getPart().getId()).get();

                    taskToSave.setCar(currentCar);
                    taskToSave.setMechanic(currentMechanic);
                    taskToSave.setPart(currentPart);

                    this.taskRepository.saveAndFlush(taskToSave);

                    sb.append(String.format(VALID_TASK_INPUT, task.getPrice()));
                }
                continue;
            } else {
                sb.append(INVALID_TASK).append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    @Override
    public String getCoupeCarTasksOrderByPrice() {

        List<Task> cars = this.taskRepository.findAllByCar_CarTypeOrderByPriceDesc(CarType.coupe).get();

        for (Task car : cars) {
            sb.append(car.toString()).append(System.lineSeparator());
        }

        return sb.toString();

    }
}
