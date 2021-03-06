package serializer;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import junit.framework.TestCase;

@RunWith(Parameterized.class)
public class FieldOrderTest extends TestCase {
	
	/*
	 * OLD TEST
	 * 
	 *  public void test_field_order() throws Exception {
        	Person p = new Person();
        	p.setName("njb");
        	School s = new School();
        	s.setName("llyz");
        	p.setSchool(s);
        	String json = JSON.toJSONString(p);
        	assertEquals("{\"name\":\"njb\",\"school\":{\"name\":\"llyz\"}}", json);
    	}

    	public static class Person {
        	private String name;
        	private School school;

        	public boolean isSchool() {
            	return false;
        	}

        	public School getSchool() {
            	return school;
        	}

        	public void setSchool(School school) {
            	this.school = school;
        	}

        	public String getName() {
            	return name;
        	}

        	public void setName(String name) {
            	this.name = name;
        	}

    	}

    	public static class School {
        	private String name;

        	public String getName() {
            	return name;
        	}

        	public void setName(String name) {
            	this.name = name;
        	}
    	}
     *
	 */
	
	private String personName;
	private String schoolName;
	private int defaultFeatures;
	private SerializerFeature features;
	
	private Person p;
	private School s;
	
	// Constructor
	public FieldOrderTest(String personName, String schoolName, int defaultFeatures, boolean isFeature) {
		configure(personName, schoolName, defaultFeatures, isFeature);
	}
	
	private void configure(String personName, String schoolName, int defaultFeatures, boolean isFeature) {
		this.personName = personName;
		this.schoolName = schoolName;		
		this.defaultFeatures = defaultFeatures;
		
		//It actually could be possible to select any subset of features. For simplicity reasons we will consider only two subcases:
		//SerializerFeature... features with no values && SerializerFeature... features = SerializerFeature.IgnoreNonFieldGetter
		if(isFeature)
			this.features = SerializerFeature.IgnoreNonFieldGetter;
		else
			this.features = null;
		
		this.p = new Person();
		this.p.setName(this.personName);
		this.s = new School();
		this.s.setName(this.schoolName);
		this.p.setSchool(this.s);
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> getParameters() {
		return Arrays.asList(new Object[][] {
			{"njb", "llyz", -1, true}
			// pName sName  df  feat
		});
	}
	
	@Test
	public void test_field_order() throws Exception {
		String expected = null;
		
		if(this.defaultFeatures >= 0 && this.defaultFeatures % 4 == 0)
			expected = "{name:\"" + this.personName + "\",school:{name:\"" + this.schoolName + "\"}}";
		else if(this.defaultFeatures >= 0 && this.defaultFeatures % 4 == 1)
			expected = "{\"name\":\"" + this.personName + "\",\"school\":{\"name\":\"" + this.schoolName + "\"}}";
		else if(this.defaultFeatures >= 0 && this.defaultFeatures % 4 == 2)
			expected = "{name:'" + this.personName + "',school:{name:'" + this.schoolName + "'}}";
		else if(this.defaultFeatures >= 0 && this.defaultFeatures % 4 == 3)
			expected = "{'name':'" + this.personName + "','school':{'name':'" + this.schoolName + "'}}";
		else if(this.defaultFeatures < 0 && (Math.abs(this.defaultFeatures) % 4 == 1 || Math.abs(this.defaultFeatures) % 4 == 2))
			expected = "[\n\t'" + this.personName + "',\n\t[\n\t\t'" + this.schoolName + "'\n\t]\n]";
		else
			expected = "[\n\t\"" + this.personName + "\",\n\t[\n\t\t\"" + this.schoolName + "\"\n\t]\n]";
		
		String json;
		if(this.features != null)
			json = JSON.toJSONString(this.p, this.defaultFeatures, this.features);
		else
			json = JSON.toJSONString(this.p, this.defaultFeatures);
		assertEquals(expected, json);
	}
	
    public static class Person {
        private String name;
        private School school;

        public boolean isSchool() {
            return false;
        }

        public School getSchool() {
            return school;
        }

        public void setSchool(School school) {
            this.school = school;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class School {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
	
}
