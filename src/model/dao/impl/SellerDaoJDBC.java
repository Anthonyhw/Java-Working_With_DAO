package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection con;

    public SellerDaoJDBC(Connection con) {
        this.con = con;
    }
    @Override
    public void insert(Seller sel) {
        PreparedStatement st = null;

        try {
            st = con.prepareStatement("INSERT INTO seller " +
                                          "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                                          "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, sel.getName());
            st.setString(2, sel.getEmail());
            st.setDate(3, new Date(sel.getBirthDate().getTime()));
            st.setDouble(4, sel.getBaseSalary());
            st.setInt(5, sel.getDepartment().getId());

            int rowsAffected = st.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    sel.setId(id);
                }
                DB.closeResultSet(rs);
            }else {
                throw new DbException("Erro inesperado: Nenhuma linha foi alterada!");
            }
        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller sel) {
        PreparedStatement st = null;

        try {
            st = con.prepareStatement("UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?");
            st.setString(1, sel.getName());
            st.setString(2, sel.getEmail());
            st.setDate(3, new Date(sel.getBirthDate().getTime()));
            st.setDouble(4, sel.getBaseSalary());
            st.setInt(5, sel.getDepartment().getId());
            st.setInt(6, sel.getId());

            st.executeUpdate();

        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;

        try {
            st = con.prepareStatement("DELETE FROM seller " +
                    "WHERE Id = ?");
            st.setInt(1, id);

            st.executeUpdate();

        }catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = con.prepareStatement("SELECT seller.*, department.Name as DepName " +
                                          "FROM seller INNER JOIN department " +
                                          "ON seller.DepartmentId = department.Id " +
                                          "WHERE seller.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if  (rs.next()){
                Department dep = InstantiateDepartment(rs);
                Seller sel = InstantiateSeller(rs, dep);
                return sel;
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private static Seller InstantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller sel = new Seller();
        sel.setId(rs.getInt("Id"));
        sel.setName(rs.getString("Name"));
        sel.setEmail(rs.getString("Email"));
        sel.setBaseSalary(rs.getDouble("BaseSalary"));
        sel.setBirthDate(rs.getDate("BirthDate"));
        sel.setDepartment(dep);
        return sel;
    }

    private static Department InstantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = con.prepareStatement("SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name");
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while  (rs.next()){
                Department d = map.get(rs.getInt("DepartmentId"));

                if (d == null) {
                    d  = InstantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), d);
                }

                Seller sel = InstantiateSeller(rs, d);
                list.add(sel);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department dep) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = con.prepareStatement("SELECT seller.*, department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name");
            st.setInt(1, dep.getId());

            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while  (rs.next()){
                Department d = map.get(rs.getInt("DepartmentId"));

                if (d == null) {
                    d  = InstantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), d);
                }

                Seller sel = InstantiateSeller(rs, d);
                list.add(sel);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
}
