import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class DepartmentTests {
    public static void main(String[] args) {
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();


        System.out.println("\n==========");
        System.out.println("*FindById*");
        Department dep = departmentDao.findById(3);
        System.out.println(dep);
        System.out.println("==========");

        System.out.println("\n==========");
        System.out.println("*FindAll*");
        List<Department> departments = departmentDao.findAll();
        for (Department department: departments) {
            System.out.println(department);
        }
        System.out.println("==========");

        System.out.println("\n==========");
        System.out.println("*Insert*");
        Department created = new Department(null, "Gadgets");
        departmentDao.insert(created);
        System.out.println("Inserted! new Seller Id:"  + created.getId());
        System.out.println("==========");

        System.out.println("\n==========");
        System.out.println("*Update*");
        Department toUpdate = departmentDao.findById(3);
        toUpdate.setName("Clothes");
        departmentDao.update(toUpdate);
        System.out.println(departmentDao.findById(3).getName());
        System.out.println("==========");


        System.out.println("\n==========");
        System.out.println("*Delete*");
        departmentDao.deleteById(5);
        System.out.println(departmentDao.findById(5).getName());
        System.out.println("==========");
    }
}
