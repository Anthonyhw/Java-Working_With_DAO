import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class SellerTests {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println("\n==========");
        System.out.println("*FindById*");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);
        System.out.println("==========");

        System.out.println("\n==========");
        System.out.println("*FindByDepartment*");
        Department dep = new Department(2, null);
        List<Seller> sellers = sellerDao.findByDepartment(dep);
        for (Seller sel: sellers) {
            System.out.println(sel);
        }
        System.out.println("==========");


        System.out.println("\n==========");
        System.out.println("*FindAll*");
        sellers = sellerDao.findAll();
        for (Seller sel: sellers) {
            System.out.println(sel);
        }
        System.out.println("==========");


        System.out.println("\n==========");
        System.out.println("*Insert*");
        Seller created = new Seller(null, "Rogerio", "rogerio@hotmail.com", new java.util.Date(), 2500.00, dep);
        sellerDao.insert(created);
        System.out.println("Inserted! new Seller Id:"  + created.getId());
        System.out.println("==========");


        System.out.println("\n==========");
        System.out.println("*Update*");
        Seller toUpdate = sellerDao.findById(13);
        toUpdate.setName("Agostinho Carrara");
        toUpdate.setEmail("agostinhocarrara@hotmail.com");
        sellerDao.update(toUpdate);
        System.out.println(sellerDao.findById(13).getEmail());
        System.out.println("==========");


        System.out.println("\n==========");
        System.out.println("*Delete*");
        sellerDao.deleteById(6);
        System.out.println(sellerDao.findById(6).getEmail());
        System.out.println("==========");
    }
}
