package org.iot.hotelitybackend.hotelservice.service;

import org.iot.hotelitybackend.employee.repository.EmployeeRepository;
import org.iot.hotelitybackend.hotelservice.repository.ReservationRepository;
import org.iot.hotelitybackend.hotelservice.repository.StayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StayServiceImpl implements StayService {

	private final StayRepository stayRepository;
	private final ModelMapper mapper;
	private final EmployeeRepository employeeRepository;
	private final ReservationRepository reservationRepository;

	@Autowired
	public StayServiceImpl(StayRepository stayRepository, ModelMapper mapper, EmployeeRepository employeeRepository,
		ReservationRepository reservationRepository) {
		this.stayRepository = stayRepository;
		this.mapper = mapper;
		this.employeeRepository = employeeRepository;
		this.reservationRepository = reservationRepository;
	}


}
