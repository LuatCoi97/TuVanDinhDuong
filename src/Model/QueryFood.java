/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Data.AllFood;
import Data.Calo;
import Data.Food;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QueryFood {

	public static ArrayList<Calo> SearchDsFood(String tablemonan, String buaan, float calo) throws SQLException {
		try (Connection conn = ConnectSQL.connectsql()) {
			ArrayList<Calo> dsfood = new ArrayList<>();
			String query = "SELECT id,tong_calo FROM " + tablemonan + " WHERE bua_an = ? AND tong_calo < ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, buaan);
			ps.setFloat(2, calo + 30);
			ResultSet rs;
			rs = ps.executeQuery();
			while (rs.next()) {
				Calo dscalo = new Calo();
				String tongcalo = rs.getString("tong_calo");
				String id = rs.getString("id");
				dscalo.setId(id);
				dscalo.setCalo(tongcalo);
				dsfood.add(dscalo);
			}
			conn.close();
			return dsfood;
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
		return null;
	}

	public static ArrayList<Calo> SearchDsFoodToFavorite(String tablemonan, String buaan, float calo, String sothich)
			throws SQLException {
		try (Connection conn = ConnectSQL.connectsql()) {
			ArrayList<Calo> dsfood = new ArrayList<>();
			String query = "SELECT id,tong_calo FROM " + tablemonan
					+ " WHERE bua_an = ? AND tong_calo < ? AND nhom = ?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, buaan);
			ps.setFloat(2, calo + 30);
			ps.setString(3, sothich);
			ResultSet rs;
			rs = ps.executeQuery();
			while (rs.next()) {
				Calo dscalo = new Calo();
				String tongcalo = rs.getString("tong_calo");
				String id = rs.getString("id");
				dscalo.setId(id);
				dscalo.setCalo(tongcalo);
				dsfood.add(dscalo);
			}
			conn.close();
			return dsfood;
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
		return null;
	}

	public static ArrayList<Food> SearchFood(ArrayList<String> id, String tablemonan, String buaan)
			throws SQLException {
		try (Connection conn = ConnectSQL.connectsql()) {
			ArrayList<Food> array = new ArrayList<>();
			for (int i = 0; i < id.size(); i++) {
				String query = "SELECT ten_mon_an, don_vi, tong_calo FROM " + tablemonan + " WHERE id = ?";
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, id.get(i));
				ResultSet rs;
				rs = ps.executeQuery();
				while (rs.next()) {
					Food food = new Food();
					String tenmon = rs.getString("ten_mon_an");
					String donvi = rs.getString("don_vi");
					String tongcalo = rs.getString("tong_calo");
					food.setTenmon(tenmon);
					food.setSoluong(donvi);
					food.setCalo(tongcalo);
					array.add(food);
				}
			}
			conn.close();
			return array;
		} catch (Exception e) {
			System.out.println("error: " + e);
		}
		return null;
	}

	public static boolean InsertFood(String tenmon, String soluong, String tongcalo, String buaan, String nhom)
			throws SQLException {
		try (Connection conn = ConnectSQL.connectsql()) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date date = new Date();
			String dateNow = dateFormat.format(date);
			String created_at = dateNow;
			String updated_at = dateNow;
			String id = null;
			String query1 = "SELECT id FROM table_mon_an WHERE ten_mon_an = ? AND don_vi = ? AND tong_calo = ? AND bua_an = ? AND nhom = ?";
			PreparedStatement ps1 = conn.prepareStatement(query1);
			ps1.setString(1, tenmon);
			ps1.setString(2, soluong);
			ps1.setString(3, tongcalo);
			ps1.setString(4, buaan);
			ps1.setString(5, nhom);
			ResultSet rs1;
			rs1 = ps1.executeQuery();
			while (rs1.next()) {
				id = rs1.getString("id");
			}
			System.out.println("id " + id);
			if (id != null) {
				return false;
			} else {
				String query = "INSERT INTO table_mon_an(ten_mon_an, don_vi, tong_calo, bua_an, nhom, created_at, updated_at)"
						+ " VALUES(?,?,?,?,?,?,?)";
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, tenmon);
				ps.setString(2, soluong);
				ps.setString(3, tongcalo);
				ps.setString(4, buaan);
				ps.setString(5, nhom);
				ps.setString(6, created_at);
				ps.setString(7, updated_at);
				ps.executeUpdate();
			}
			conn.close();
			return true;
		} catch (Exception e) {
			System.err.println("error: " + e);
			;
		}
		return false;
	}

	/*
	 * get all name food
	 */
	public static ArrayList<AllFood> getAllFood() throws SQLException {
		ArrayList<AllFood> arrayList = new ArrayList<>();
		try (Connection conn = ConnectSQL.connectsql()) {
			String query = "SELECT id,ten_mon_an,don_vi,tong_calo,bua_an,nhom FROM hcstt.table_mon_an;";
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs;
			rs = ps.executeQuery();
			while (rs.next()) {
				AllFood allFood = new AllFood();
				String id = rs.getString("id");
				String mon = rs.getString("ten_mon_an");
				String so_luong = rs.getString("don_vi");
				String tong_calo = rs.getString("tong_calo");
				String bua_an = rs.getString("bua_an");
				String nhom = rs.getString("nhom");
				allFood.setId(id);
				allFood.setTenmon(mon);
				allFood.setSoluong(so_luong);
				allFood.setCalo(tong_calo);
				allFood.setBuaAn(bua_an);
				allFood.setNhom(nhom);
				arrayList.add(allFood);
			}
			conn.close();
			return arrayList;
		} catch (Exception e) {

		}
		return null;
	}

	public static ArrayList<String> getFavoriteFood() {
		ArrayList<String> favorite = new ArrayList<String>();
		try (Connection conn = ConnectSQL.connectsql()) {
			String querry = "SELECT so_thich FROM favorite_user";
			PreparedStatement ps = conn.prepareStatement(querry);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String a = rs.getString("so_thich");
				favorite.add(a);
			}
			return favorite;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
