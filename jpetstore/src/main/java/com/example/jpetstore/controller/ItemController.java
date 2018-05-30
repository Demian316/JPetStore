package com.example.jpetstore.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.example.jpetstore.service.ItemValidator;
import com.example.jpetstore.service.PetStoreFacade;

@Controller
@SessionAttributes("itemForm")
public class ItemController {
	@Autowired
	private PetStoreFacade petStore;
	
	@Autowired
	private ItemValidator itemValidator;
	
	@ModelAttribute("itemForm") //Command 체의 이름 지정 
	public ItemForm createItemForm() {
		return new ItemForm();
	}
	
	@ModelAttribute("categoryNames") //Command 객체의 이름 지정 
	public List<String> referenceData() {
		ArrayList<String> categoryNames = new ArrayList<String>();
		categoryNames.add("FISH");
		categoryNames.add("DOGS");
		categoryNames.add("REPTILES");
		categoryNames.add("CATS");
		categoryNames.add("BIRDS");
		
		return categoryNames;
	}
	
	@ModelAttribute("productNames") 
	public List<String> referenceData2() {
		ArrayList<String> productNames = new ArrayList<String>();
		productNames.add("Angelfish");
		productNames.add("Tiger Shark");
		productNames.add("Koi");
		productNames.add("Goldfish");
		productNames.add("Bulldog");
		productNames.add("Poodle");
		productNames.add("Dalmation");
		productNames.add("Golden Retriever");
		productNames.add("Labrador Retriever");
		productNames.add("Chihuahua");
		productNames.add("Rattlesnake");
		productNames.add("Iguana");
		productNames.add("Manx");
		productNames.add("Persian");
		productNames.add("Amazon Parrot");
		productNames.add("Finch");

		return productNames;
	}
	
	@RequestMapping("/shop/addItem.do") //addItem.do의 요청을 mapping 
	public String addNewItem(HttpServletRequest request,
			@ModelAttribute("itemForm") ItemForm itemForm //ItemForm.java -> 아이템 정보를 저장할 객체.이 안에 Item 객체 존재. 
			) throws ModelAndViewDefiningException {
		UserSession userSession = (UserSession) request.getSession().getAttribute("userSession");
		if(userSession != null) { //UserSession 이 있는 경우 -> 로그인 함. NewItemForm을 return;
			return "NewItemForm";
		}
		else { 
			//UserSession 이 없는 경우 -> 로그인 하지 않으면 물품 등록을 하지 못하도록 막음 
			//에러메세지 출력하게
			ModelAndView modelAndView = new ModelAndView("Error");
			modelAndView.addObject("message", "물품을 등록하시려면 먼저 로그인 하세요.");
			System.out.println(modelAndView);
			throw new ModelAndViewDefiningException(modelAndView);
		}
	}
	
	@RequestMapping("/shop/newItemSubmitted.do")
	public String bindAndValidateOrder(HttpServletRequest request,
			@ModelAttribute("itemForm") ItemForm itemForm,
			BindingResult result) {
		//User 이름이 전달이 안되어서 지정해줌 
		UserSession userSession = (UserSession)request.getSession().getAttribute("userSession");
		itemForm.getItem().setSellerUsername(userSession.getAccount().getUsername());

//		System.out.println(itemForm.getItem().getAttribute1());
		itemValidator.validateItemFields(itemForm.getItem(), result);

		if(result.hasErrors()) {
			return "NewItemForm";
		}
		else {
			return "confirmAddItem";
		}
	}
	
	@RequestMapping("/shop/confirmAddItem.do")
	protected ModelAndView confirmAddItem(
			@ModelAttribute("itemForm") ItemForm itemForm,
			SessionStatus status) {
		System.out.println("Item has been added.");
		return null;
	}
	
}
