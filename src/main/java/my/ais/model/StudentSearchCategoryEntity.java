package my.ais.model;

import java.util.Arrays;
import java.util.List;


public class StudentSearchCategoryEntity {
	
	//TODO delete this
	public static List<String> getStatus() {
        return Arrays.asList(new String[]{"ACTIVE",
        									"INACTIVE - MEDICAL LEAVE",
        									"INACTIVE - TANGGUH PENGAJIAN",
        									"GRADUATED",
        									"BERHENTI"});
    }
	
	//TODO delete this
	public static List<String> getProgram() {
        return Arrays.asList(new String[]{"MANA - Master of Science (Information Assurance)",
        									"MANN - Master of Science (Computer System Engineering)",
        									"MANP - Master of Software Engineering",
        									"MANQ - Master of Science (Informatics)",
        									"MANG - Master of Philosophy",
        									"PANP - Doctor of Software Engineering",
        									"PANG - Doctor of Philosophy"});
    }	
	
	public static List<String> getMode() {
        return Arrays.asList(new String[]{"FT", "PT"});
    }
	
	public static List<String> getCourseType() {
        return Arrays.asList(new String[]{"COURSEWORK", "RESEARCH", "MIXED MODE"});
    }
	
	public static List<String> getLevel() {
        return Arrays.asList(new String[]{"MASTER", "PHD"});
    }
 
    public static List<String> getCountry() {
        return Arrays.asList(new String[]{"LOCAL","INTERNATIONAL","Afghanistan","Åland Islands","Albania","Algeria",
        		"American Samoa","Andorra","Angola","Anguilla","Antarctica","Antigua And Barbuda",
        		"Argentina","Armenia","Aruba","Australia","Austria","Azerbaijan","Bahamas",
        		"Bahrain","Bangladesh","Barbados","Belarus","Belgium","Belize","Benin","Bermuda",
        		"Bhutan","Bolivia","Bosnia And Herzegovina","Botswana","Bouvet Island","Brazil",
        		"British Indian Ocean Territory","Brunei Darussalam","Bulgaria","Burkina Faso",
        		"Burundi","Cambodia","Cameroon","Canada","Cape Verde","Cayman Islands",
        		"Central African Republic","Chad","Chile","China","Christmas Island","Cocos (Keeling) Islands",
        		"Colombia","Comoros","Congo The Democratic Republic Of The","Cook Islands","Costa Rica",
        		"Cote D'ivoire","Croatia","Cuba","Cyprus","Czech Republic","Denmark","Djibouti","Dominica",
        		"Dominican Republic","Ecuador","Egypt","El Salvador","Equatorial Guinea","Eritrea",
        		"Estonia","Ethiopia","Falkland Islands (Malvinas)","Faroe Islands","Fiji","Filipina","Finland",
        		"France","French Guiana","French Polynesia","French Southern Territories","Gabon",
        		"Gambia","Georgia","Germany","Ghana","Gibraltar","Greece","Greenland","Grenada",
        		"Guadeloupe","Guam","Guatemala","Guernsey","Guinea","Guinea-bissau","Guyana",
        		"Haiti","Heard Island And Mcdonald Islands","Holy See (Vatican City State)",
        		"Honduras","Hong Kong","Hungary","Iceland","India","Indonesia","Iran","Iraq",
        		"Ireland","Isle Of Man","Italy","Jamaica","Japan","Jersey","Jordan","Kazakhstan",
        		"Kenya","Kiribati","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho",
        		"Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macao","Macedonia",
        		"Yugoslavia","Madagascar","Malawi","Malaysia","Maldives","Mali","Malta",
        		"Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico",
        		"Micronesia","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Morocco",
        		"Mozambique","Myanmar","Namibia","Nauru","Nepal","Netherlands","Netherlands Antilles",
        		"New Caledonia","New Zealand","Nicaragua","Nigeria","Niue","Norfolk Island","North Korea",
        		"Northern Mariana Islands","Norway","Oman","Pakistan","Palau","Palestin",
        		"Panama","Papua New Guinea","Paraguay","Peru",/*"Philippines",*/"Pitcairn",
        		"Poland","Portugal","Puerto Rico","Qatar","Romania","Russia","Rwanda","Saint Helena",
        		"Saint Kitts And Nevis","Saint Lucia","Saint Pierre And Miquelon",
        		"Saint Vincent And The Grenadines","Samoa","San Marino","Sao Tome And Principe",
        		"Saudi Arabia","Senegal","Serbia","Seychelles","Sierra Leone","Singapore",
        		"Slovakia","Slovenia","Solomon Islands","Somalia","South Africa",
        		"South Georgia And The South Sandwich Islands","South Korea","Spain",
        		"Sri Lanka","Sudan","Suriname","Svalbard And Jan Mayen","Swaziland",
        		"Sweden","Switzerland","Syrian Arab Republic","Taiwan","Tajikistan",
        		"Tanzania","Thailand","Timor Leste","Togo","Tokelau","Tonga","Trinidad And Tobago",
        		"Tunisia","Turki","Turkmenistan","Turks And Caicos Islands","Tuvalu","Uganda",
        		"Ukraine","United Arab Emirates","United Kingdom","United States",
        		"United States Minor Outlying Islands","Uruguay","Uzbekistan","Vanuatu",
        		"Venezuela","Vietnam","Virgin Islands","Wallis And Futuna","Western Sahara",
        		"Yemen","Zambia","Zimbabwe"});
    }
}
