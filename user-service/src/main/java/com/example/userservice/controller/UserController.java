package com.example.userservice.controller;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import com.example.userservice.dto.Mail;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.ReportTotal;
import com.example.userservice.entity.User;
import com.example.userservice.service.CloudinaryService;
import com.example.userservice.service.MailService;
import com.example.userservice.service.UserService;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;


@CrossOrigin("*")
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@Autowired
	MailService mailService;


	@Autowired
	CloudinaryService cloudinaryService;
	
	@Autowired
    private ModelMapper modelMapper;
	//Test
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void CreateOrder(@RequestBody UserRequest userRequest) {
		userService.createUser(userRequest);
	}
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponse> GetAllOrder() {
		return userService.getAllUserTest();
	}
	@RequestMapping("/findbyid")
	@Transactional(readOnly=true)
	@SneakyThrows
	public Boolean FindById(@RequestParam String id) {
	    return userService.findbyid(id).isPresent();
	}
	
	
	//main
	
	
	@GetMapping(path = "/getalluser")
	public ResponseEntity<List<User>> GetAllUser(){
		List<User> listcaUser = userService.findByRole("user");
		return new ResponseEntity<>(listcaUser, HttpStatus.OK);
	}
	
	@GetMapping(path = "/findbyidandrole")
	@Transactional(readOnly=true)
	public ResponseEntity<UserDto> findByIdAndRole(String id, String role){
		User userFind = userService.findByIdAndRole(id, role);
		UserDto userDto = modelMapper.map(userFind, UserDto.class);
		return new ResponseEntity<>(userDto, HttpStatus.OK);
	}
	

	@GetMapping(path = "/login")
	public ResponseEntity<User> Login(String id, String password) {
		User userFind = userService.findByIdAndRole(id, "user");
		
		if (userFind != null && userFind.getPassword() != null) {
			String decodedValue = new String(Base64.getDecoder().decode(userFind.getPassword()));
			System.out.println(userFind);
			if (password.equals(decodedValue)) {
				userFind.setPassword(decodedValue);
				return new ResponseEntity<>(userFind, HttpStatus.OK);
			}
			else {
				return null;
			}
		}
		else
			return new ResponseEntity<>(userFind, HttpStatus.OK);
	}

	@PostMapping(path = "/signup", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<User> SignUp(String username, String fullname, String email, String password) {
		User user = userService.findByIdAndRole(username, "user");
		if (user != null) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		} else {
			String encodedValue = Base64.getEncoder().encodeToString(password.getBytes());
			String avatar = "https://haycafe.vn/wp-content/uploads/2022/02/Avatar-trang-den.png";
			User newUser = userService.saveUser(new User(username, "default", "user", encodedValue, fullname, avatar,
					email, null, null, null));
			System.out.println(newUser);
			return new ResponseEntity<>(newUser, HttpStatus.OK);
		}
	}

	@PostMapping(path = "/forgot", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ForgotPassword(String id) {
		User user = userService.findByIdAndRole(id, "user");
		if (user != null) {
			int code = (int) Math.floor(((Math.random() * 899999) + 100000));
			Mail mail = new Mail();
			mail.setMailFrom("n20dccn022@student.ptithcm.edu.vn");
			mail.setMailTo(user.getEmail());
			mail.setMailSubject("For got Password");
			mail.setMailContent("Your code is: " + code);
			mailService.sendEmail(mail);
			session.setAttribute("code", code);
			System.out.println(code);
			return new ResponseEntity<String>(new Gson().toJson(String.valueOf(code)), HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}

	@PostMapping(path = "/forgotnewpass", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ForgotNewPass(String id, String code, String password) {
//		String codeSession = (String) session.getAttribute("code");
//		System.out.println("session: "+ codeSession);
		User user = userService.findByIdAndRole(id, "user");
		if (user != null) {
			String encodedValue = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPassword(encodedValue);
			userService.saveUser(user);
			return new ResponseEntity<String>(password, HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
	}

	@PostMapping(path = "changepassword", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ChangePassword(String id, String password) {
		User user = userService.findByIdAndRole(id, "user");
		if (user != null) {
			String encodedValue = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPassword(encodedValue);
			userService.saveUser(user);
			return new ResponseEntity<String>(password, HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
	}

	@PostMapping(path = "update", consumes = "multipart/form-data")
	public ResponseEntity<User> UpdateAvatar(String id, MultipartFile avatar, String fullname, String email,
			String phoneNumber, String address) {
		User user = userService.findByIdAndRole(id, "user");
		if (user != null) {
			if (avatar !=null) {
				String url = cloudinaryService.uploadFile(avatar);
				user.setAvatar(url);
			}
			user.setUser_Name(fullname);
			user.setEmail(email);
			user.setPhone_Number(phoneNumber);
			user.setAddress(address);
			userService.saveUser(user);
			if(user.getPassword()!=null)
				user.setPassword(new String(Base64.getDecoder().decode(user.getPassword())));
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@PostMapping(path = "google", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<User> LoginWithGoogle(String id, String fullname, String email, String avatar) {
		User user = userService.findByIdAndRole(id, "user");
		if (user == null) {
			user = userService
					.saveUser(new User(id, "google", "user", null, fullname, avatar, email, null, null, null));
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	//Admin	
	
	@GetMapping(path = "/loginAdmin")
	public ResponseEntity<User> LoginAdmin(String id, String password) {
		System.out.println(id);
		User userFind = userService.findByIdAndRole(id, "admin");
		if (userFind != null && userFind.getPassword() != null) {
			String decodedValue = new String(Base64.getDecoder().decode(userFind.getPassword()));
			System.out.println(userFind);
			if (password.equals(decodedValue)) {
				userFind.setPassword(decodedValue);
				return new ResponseEntity<>(userFind, HttpStatus.OK);
			}
			else {
				return null;
			}
		}
		else
			return new ResponseEntity<>(userFind, HttpStatus.OK);
	}
	@PostMapping(path = "changepasswordadmin", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ChangePasswordAdmin(String id, String password) {
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			String encodedValue = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPassword(encodedValue);
			userService.saveUser(user);
			return new ResponseEntity<String>(password, HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
	}
	
	@PostMapping(path = "updateadmin", consumes = "multipart/form-data")
	public ResponseEntity<User> UpdateAvatarAdmin(String id, MultipartFile avatar, String fullname, String email,
			String phoneNumber, String address) {
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			if (avatar !=null) {
				String url = cloudinaryService.uploadFile(avatar);
				user.setAvatar(url);
			}
			user.setUser_Name(fullname);
			user.setEmail(email);
			user.setPhone_Number(phoneNumber);
			user.setAddress(address);
			userService.saveUser(user);
			if(user.getPassword()!=null)
				user.setPassword(new String(Base64.getDecoder().decode(user.getPassword())));
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	@PostMapping(path = "/forgotadmin", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ForgotPasswordAdmin(String id) {
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			int code = (int) Math.floor(((Math.random() * 899999) + 100000));
			Mail mail = new Mail();
			mail.setMailFrom("n20dccn022@student.ptithcm.edu.vn");
			mail.setMailTo(user.getEmail());
			mail.setMailSubject("For got Password");
			mail.setMailContent("Your code is: " + code);
			mailService.sendEmail(mail);
			session.setAttribute("code", code);
			System.out.println(code);
			return new ResponseEntity<String>(new Gson().toJson(String.valueOf(code)), HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	}
	@PostMapping(path = "/forgotnewpassadmin", consumes = "application/x-www-form-urlencoded")
	public ResponseEntity<String> ForgotNewPassAdmin(String id, String code, String password) {
//		String codeSession = (String) session.getAttribute("code");
//		System.out.println("session: "+ codeSession);
		User user = userService.findByIdAndRole(id, "admin");
		if (user != null) {
			String encodedValue = Base64.getEncoder().encodeToString(password.getBytes());
			user.setPassword(encodedValue);
			userService.saveUser(user);
			return new ResponseEntity<String>(password, HttpStatus.OK);
		} else
			return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
	}
	
//	@GetMapping(path = "/age-group")
//    public ResponseEntity<List<ReportTotal>> ageGroup() {
//  
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = userService.findAgeGroup();
//            List<ReportTotal> reportTotals = new ArrayList<>();
//            System.out.println(results);
//            for (Object[] result : results) {
//                String name = (String) result[0];
//                BigDecimal value = (BigDecimal) result[1];
//                reportTotals.add(new ReportTotal(name, value.doubleValue()));
//            }
//
//            if (reportTotals.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
//        }
//    }
//	@GetMapping(path = "/age-group-revenue")
//    public ResponseEntity<List<Object[]>> ageGroupRevenue() {
//  
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = userService.findAgeGroupRevenue();
//            if (results.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(results, HttpStatus.OK);
//        }
//    }
	//thống kê
//	@GetMapping(path = "/revenue-statistic")
//    public ResponseEntity<List<ReportTotal>> reportTotal(@RequestParam("dateFrom") String dateFromString,
//                                                         @RequestParam("dateTo") String dateToString) {
//       
//            Date dateFrom = java.sql.Date.valueOf(dateFromString);
//            Date dateTo = java.sql.Date.valueOf(dateToString);
//
//
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = productService.findRevenueStatisticByDate(dateFrom, dateTo);
//            List<ReportTotal> reportTotals = new ArrayList<>();
//            System.out.println(results);
//            for (Object[] result : results) {
//                String name = (String) result[0];
//                BigDecimal value = (BigDecimal) result[1];
//                reportTotals.add(new ReportTotal(name, value.doubleValue()));
//            }
//
//            if (reportTotals.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
//        }
//    }
//	@GetMapping(path = "/month-statistic")
//    public ResponseEntity<List<ReportTotal>> monthStatistic(@RequestParam("year") String year) {
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//		int yearInt = Integer.parseInt(year); 
//            List<Object[]> results = productService.findStatisticByMonth(yearInt);
//            List<ReportTotal> reportTotals = new ArrayList<>();
//            System.out.println(results);
//            for (Object[] result : results) {
//                String name = String.valueOf( result[0]);
//                BigDecimal value = (BigDecimal) result[1];
//                reportTotals.add(new ReportTotal(name, value.doubleValue()));
//            }
//
//            if (reportTotals.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
//        }
//    }
//	@GetMapping(path = "/quantity-statistic")
//    public ResponseEntity<List<ReportTotal>> quantityStatistic(@RequestParam("dateFrom") String dateFromString,
//                                                         @RequestParam("dateTo") String dateToString) {
//       
//            Date dateFrom = java.sql.Date.valueOf(dateFromString);
//            Date dateTo = java.sql.Date.valueOf(dateToString);
//
//
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = productService.findQuantityStatisticByDate(dateFrom, dateTo);
//            List<ReportTotal> reportTotals = new ArrayList<>();
//            System.out.println(results);
//            for (Object[] result : results) {
//                String name = (String) result[0];
//                BigDecimal value = (BigDecimal) result[1];
//                reportTotals.add(new ReportTotal(name, value.doubleValue()));
//            }
//
//            if (reportTotals.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
//        }
//    }
//	@GetMapping(path = "/product-statistic")
//    public ResponseEntity<List<ReportTotal>> ProductStatistic() {
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = productService.findProductStatistic();
//            List<ReportTotal> reportTotals = new ArrayList<>();
//            for (Object[] result : results) {
//                String name = (String) result[0];
//                BigDecimal value = (BigDecimal) result[1];
//                reportTotals.add(new ReportTotal(name, value.doubleValue()));
//            }
//            if (reportTotals.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
//        }
//    }
//	@GetMapping(path = "/unit-of-product-statistic")
//    public ResponseEntity<List<ReportTotal>> UnitOfProductStatistic() {
//            // Gọi phương thức để thực hiện xử lý với các đối tượng Date này
//            List<Object[]> results = productService.findUnitOfProductStatistic();
//            List<ReportTotal> reportTotals = new ArrayList<>();
//            for (Object[] result : results) {
//                String name = (String) result[0];
//                BigDecimal value = (BigDecimal) result[1];
//                reportTotals.add(new ReportTotal(name, value.doubleValue()));
//            }
//            if (reportTotals.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            } else {
//                return new ResponseEntity<>(reportTotals, HttpStatus.OK);
//        }
//	}

}
