package hibernate.util;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.w3c.dom.Element;

import modelo.Insumo;
import modelo.Movimiento;
import modelo.OrdenDeCompra;
import modelo.Proveedor;
import modelo.Remito;
import modelo.Categoria;
import modelo.DetalleFactura;
import modelo.DetalleOrdenDeCompra;
import modelo.DetalleRemito;
import modelo.Factura;
import main.AppMain;
import modelo.Sector;
import modelo.Sucursal;
import modelo.Usuario;

public class CRUD {
	
	
	public CRUD() {
		
	}
	
	
/****************************************************************************************************
**********************************    Queries   *****************************************************
*****************************************************************************************************/
	
	/********************************* G E N E R A L E S ********************************/
	
	public static void guardarObjeto(Object objeto) {
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			appMain.getSession().save(objeto);
			
			tx.commit();
			
//			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	public static void actualizarObjeto(Object objeto) {
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			appMain.getSession().update(objeto);
			
			tx.commit();
			
//			appMain.getSession().close();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
	}
	
	
	
	/********************************** U S U A R I O ************************************/

	/**** busco en tabla usuarioSector q usuarios estan relacionados con el
	 * 		idSectorIN, y tomo solamente de esos usuarios sus estados retornandolos en una lista
	 */
	public static List<Usuario> obtenerListaUsuariosPorIdSectorJoinUsuarioSector(Integer idSectorIN) {
		List<Usuario> listaUsuarios = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			/** 
			 * 	LOS  "ON" en el "INNER JOIN" ====> no lo toma HQL, en vez de eso, se utiliza el "AND" en el "WHERE"
			 */
			Query query = appMain.getSession().createQuery("select new Usuario(u.estadoUsuario) "
					+ "from UsuarioSector uS "
					+ "inner join uS.usuario u "
					+ "inner join uS.sector s "
					+ "where uS.sector = :id "
					+ "and uS.usuario = u.pkDniUsuario "
					+ "and uS.sector = s.idSector");

			query.setInteger("id", idSectorIN);
			listaUsuarios = query.list();
			
			appMain.getSession().close();
			
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaUsuarios;
	}
	
	
	
	/********************************** S E C T O R ************************************/
	
	public static Sector obtenerSectorPorNombre(String nombreSectorIN) {
		Transaction tx = null;
		Sector sector = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector where Nombre_Sector= :id");
			query.setString("id", nombreSectorIN);
			sector = (Sector) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return sector;
	}

	
	public static List<Sector> obtenerListaSectoresActivos() {
		List<Sector> listaSectores = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector "
					+ "where estadoSector= :estado");
			query.setString("estado", "Activo");
			listaSectores = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaSectores;
	}
	
	
	public static Sector obtenerSectorPorId(Integer idSectorIN) {
		Transaction tx = null;
		Sector sector = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sector where idSector= :id");
			query.setInteger("id", idSectorIN);
			sector = (Sector) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return sector;
	}
	
	
	/********************************** C A T E G O R I A ************************************/

	public static List<Categoria> obtenerListaCategoriasPorIdSector(Integer idSectorIN) {
		List<Categoria> listaCategorias = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Categoria "
					+ "where sector= :id");
			query.setInteger("id", idSectorIN);
			listaCategorias = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaCategorias;
	}
	
	
	/**** este query solo retorna un objeto categoria con su id completa, los demas atributos vacios ****/
	public static Categoria obtenerCategoriaPorNombreCategoriaYPorIdSector1(String nombreCategoriaIN,
																		Integer idSectorCategoriaIN) {
		Transaction tx = null;
		Categoria  categoria = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Categoria(c.pkIdCategoria) "
					+ "from Categoria c "
					+ "where nombreCategoria= :nombre "
					+ "and sector= :id");
			query.setString("nombre", nombreCategoriaIN);
			query.setInteger("id", idSectorCategoriaIN);
			categoria = (Categoria) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return categoria;
	}
	
	
	/*** este query es igual q el 1, solo q ademas de retornar el objeto categoria con su id, tambien tiene completo el nombre ***/
	public static Categoria obtenerCategoriaPorNombreCategoriaYPorIdSector2(String nombreCategoriaIN, Integer idSectorCategoriaIN) {
		Transaction tx = null;
		Categoria  categoria = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Categoria(c.pkIdCategoria, c.nombreCategoria) "
			+ "from Categoria c "
			+ "where nombreCategoria= :nombre "
			+ "and sector= :id");
			query.setString("nombre", nombreCategoriaIN);
			query.setInteger("id", idSectorCategoriaIN);
			categoria = (Categoria) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return categoria;
	}
	
	
	/**** este query retorna un objeto categoria con todos sus atributos completos ****/
	public static Categoria obtenerCategoriaPorNombreCategoriaYPorIdSector3(String nombreCategoriaIN,
																		Integer idSectorCategoriaIN) {
		Transaction tx = null;
		Categoria  categoria = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Categoria "
					+ "where nombreCategoria= :nombre "
					+ "and sector= :id");
			query.setString("nombre", nombreCategoriaIN);
			query.setInteger("id", idSectorCategoriaIN);
			categoria = (Categoria) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return categoria;
	}
	
	
	public static Categoria obtenerCategoriaPorId(Integer idCategoriaIN) {
		Transaction tx = null;
		Categoria  categoria = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Categoria "
					+ "where pkIdCategoria= :id");
			query.setInteger("id", idCategoriaIN);
			categoria = (Categoria) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return categoria;
	}

	
	public static List<Categoria> obtenerListaCategoriasActivasPorIdSector(Integer idSectorIN) {
		List<Categoria> listaCategorias = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Categoria "
					+ "where sector= :id "
					+ "and estadoCategoria= :estado");
			query.setInteger("id", idSectorIN);
			query.setString("estado", "Activo");
			listaCategorias = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaCategorias;
	}
	
	
	/********************************** I N S U M O ************************************/

	/**devuelve lista de insumos x idCategoria, pero sin acceso al nombre categoria**/
	public static List<Insumo> obtenerListaInsumosPorIdCategoria1(Integer idCategoriaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where categoria= :id");
			query.setInteger("id", idCategoriaIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/**devuelve lista de insumos x idCategoria, con acceso al nombre categoria**/
	public static List<Insumo> obtenerListaInsumosPorIdCategoria2(Integer idCategoriaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, ins.nroLote, ins.nroArticulo, ins.referencia, ins.unidadMedida, "
					+ "ins.stockActual, ins.fechaIngreso, ins.fechaBaja, ins.fechaVencimiento, ins.precioInsumo, ins.temperatura, "
					+ "ins.categoria, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.categoria cat "
					+ "where ins.categoria= :id "
					+ "and ins.categoria = cat.pkIdCategoria");
			query.setInteger("id", idCategoriaIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}

	
	/** calcula el stock gral de los insumos, de acuerdo a un categoria y lo retorna como una lista de insumos **/
	public static List<Insumo> obtenerListaInsumosStrockGralPorIdCategoria(Integer idCategoriaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, ins.unidadMedida, sum(ins.stockActual), ins.pdp, ins.estadoInsumo) "
					+ "from Insumo ins "
					+ "where categoria = :idCategoria "
					+ "group by ins.nombreInsumo");
			query.setInteger("idCategoria", idCategoriaIN);
    		
    		listaInsumos = query.list();
    		
    		appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** calcula el stock gral de los insumos, de acuerdo a un categoria y lo retorna como una lista de insumos (ademas trae nombre sucursal) 
	 * solo tiene en cuenta insumos ACTIVOS **/
	public static List<Insumo> obtenerListaInsumosStrockGralPorIdCategoria2(Integer idCategoriaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, ins.unidadMedida, sum(ins.stockActual), ins.pdp, "
					+ "ins.estadoInsumo, suc.nombreSucursal) "
					+ "from Insumo ins "
					+ "inner join ins.sucursal suc "
					+ "where ins.categoria = :idCategoria "
					+ "and ins.estadoInsumo = 'Activo' "
					+ "group by ins.nombreInsumo, suc.nombreSucursal");
			query.setInteger("idCategoria", idCategoriaIN);
    		
    		listaInsumos = query.list();
    		
    		appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	
	/** calcula el stock gral de los insumos que esten igual o x debajo de su PDP, de acuerdo a un categoria y lo retorna como una lista de insumos **/
	public static List<Insumo> obtenerListaInsumosStrockGralPdpPorIdCategoria(Integer idCategoriaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, sum(ins.stockActual), ins.pdp, ins.categoria, cat.nombreCategoria, "
					+ "sec.nombreSector, ins.nroArticulo , ins.referencia, pro.nombreProveedor) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor pro "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where ins.categoria = :idCategoria "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and ins.sucursal = '1' "
					+ "group by ins.nombreInsumo "
					+ "having sum(ins.stockActual) <= ins.pdp");
			query.setInteger("idCategoria", idCategoriaIN);
    		
    		listaInsumos = query.list();
    		
    		appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** devuelve una lista con todos los insumos q esten igual o x debajo de su PDP, de acuerdo a un idSector **/
	public static List<Insumo> obtenerListaInsumosStrockGralPdpPorIdSector(Integer idSectorIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, sum(ins.stockActual), ins.pdp, ins.categoria, cat.nombreCategoria, "
					+ "sec.nombreSector, ins.nroArticulo , ins.referencia, "
					+ "pro.nombreProveedor) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor pro "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where sec.idSector = :idSector "
					+ "and ins.sucursal = '1' "
					+ "group by ins.nombreInsumo "
					+ "having sum(ins.stockActual) <= ins.pdp");
			query.setInteger("idSector", idSectorIN);
    		
    		listaInsumos = query.list();
    		
    		appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** calcula el stock gral de los insumos que esten igual o x debajo de su PDP, de acuerdo a un categoria y proveedor 
	 * 		lo retorna como una lista de insumos **/
	public static List<Insumo> obtenerListaInsumosStrockGralPdpPorIdSectorAndIdProveedor(Integer idSectorIN, Integer idProveedorIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, sum(ins.stockActual), ins.pdp, ins.categoria, cat.nombreCategoria, "
					+ "sec.nombreSector, ins.nroArticulo , ins.referencia, "
					+ "pro.nombreProveedor) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor pro "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where sec.idSector = :idSector "
					+ "and ins.proveedor = :idProveedor "
					+ "and ins.sucursal = '1' "
					+ "group by ins.nombreInsumo "
					+ "having sum(ins.stockActual) <= ins.pdp");
			query.setInteger("idSector", idSectorIN);
			query.setInteger("idProveedor", idProveedorIN);
    		
    		listaInsumos = query.list();
    		
    		appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}

	
	/** la diferencia con el metodo2, es q este no crea un objeto Insumo customizado (nombreCategoria agregado)
	 * 		este crea objeto Insumo original (es decir, tal cual esta en la entidad de la BD, sin nombreCategoria)
	 */
	public static List<Insumo> obtenerListaInsumosPorNombreYPorIdCategoria1(String nombreInsumoIN, Integer idCategoriaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo= :nombre and categoria= :id");
			query.setString("nombre", nombreInsumoIN);
			query.setInteger("id", idCategoriaIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** hace exactamente que el metodo1, solo q usamos un "select"
	 * 		el cual, crea un objeto insumo customizado, ya q le agregamos
	 * 		el campo "nombreCategoria" q no forma parte de la entidad Insumo
	 */
	public static List<Insumo> obtenerListaInsumosPorNombreYPorIdCategoria2(String nombreInsumoIN, Integer idCategoriaIN, String nombreSurcursalIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, ins.nroLote, ins.nroArticulo, ins.referencia, ins.unidadMedida, "
					+ "ins.stockActual, ins.fechaIngreso, ins.fechaBaja, ins.fechaVencimiento, ins.precioInsumo, ins.temperatura, ins.nroPedido, "
					+ "ins.categoria, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.categoria cat "
					+ "inner join ins.sucursal suc "
					+ "where ins.nombreInsumo= :nombre and ins.categoria= :id "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and suc.nombreSucursal= :nombreSuc");
			query.setString("nombre", nombreInsumoIN);
			query.setInteger("id", idCategoriaIN);
			query.setString("nombreSuc", nombreSurcursalIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	public static List<Insumo> obtenerListaInsumosPorNombre(String nombreInsumoIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, ins.nroLote, ins.nroArticulo, ins.referencia, ins.unidadMedida, "
					+ "ins.stockActual, ins.fechaIngreso, ins.fechaBaja, ins.fechaVencimiento, ins.precioInsumo, ins.temperatura, "
					+ "ins.categoria, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.categoria cat "
					+ "where ins.nombreInsumo= :nombre "
					+ "and ins.categoria = cat.pkIdCategoria");
			query.setString("nombre", nombreInsumoIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/**
	 * tiene en cuenta sucursal
	 */
	public static List<Insumo> obtenerListaInsumosPorNombre3(String nombreInsumoIN, String nombreSucursalIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, ins.nroLote, ins.nroArticulo, ins.referencia, ins.unidadMedida, "
					+ "ins.stockActual, ins.fechaIngreso, ins.fechaBaja, ins.fechaVencimiento, ins.precioInsumo, ins.temperatura, ins.nroPedido, "
					+ "ins.categoria, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.categoria cat "
					+ "inner join ins.sucursal suc "
					+ "where ins.nombreInsumo= :nombre "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and suc.nombreSucursal= :nombreSuc");
			query.setString("nombre", nombreInsumoIN);
			query.setString("nombreSuc", nombreSucursalIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** este sirve solo para obtener insumo de igual nombre, para la posterior modificacion y actualizacion del pdp **/
	public static List<Insumo> obtenerListaInsumosPorNombre2(String nombreInsumoIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo ins "
					+ "where ins.nombreInsumo= :nombre");
			query.setString("nombre", nombreInsumoIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** obtiene insumos por nombre de sucursal diagnos = 1 **/
	public static List<Insumo> obtenerListaInsumosPorNombreSucursalDiagnos(String nombreInsumoIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo ins "
					+ "where ins.nombreInsumo= :nombre "
					+ "and ins.sucursal = 1");
			query.setString("nombre", nombreInsumoIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** obtiene insumos por nombre insumo y id sucursal **/
	public static List<Insumo> obtenerListaInsumosPorNombreYSucursal(String nombreInsumoIN, Integer idSucursalIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo ins "
					+ "where ins.nombreInsumo= :nombre "
					+ "and ins.sucursal= :idSuc");
			query.setString("nombre", nombreInsumoIN);
			query.setInteger("idSuc", idSucursalIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	public static Insumo obtenerInsumoPorNombre(String nombreIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo= :nombre");
			query.setString("nombre", nombreIN);
			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	public static Insumo obtenerInsumoPorNroArticulo(String nroArticuloIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nroArticulo= :articulo");
			query.setString("articulo", nroArticuloIN);
//			insumo = (Insumo) query.uniqueResult();
			insumo = (Insumo) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	public static List<Insumo> obtenerListaInsumosPorNroArticulo(String nroArticuloIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nroArticulo= :articulo");
			query.setString("articulo", nroArticuloIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	public static Insumo obtenerInsumoPorNombrePorNroLotePorIdCategoria(String nombreInsumoIN, String nroLoteIN, Integer idCategoriaIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo= :nombre "
					+ "and nroLote= :lote "
					+ "and categoria= :id");
			query.setString("nombre", nombreInsumoIN);
			query.setString("lote", nroLoteIN);
			query.setInteger("id", idCategoriaIN);
			insumo = (Insumo) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	public static List<Insumo> obtenerListaInsumosPorNroReferencia(String nroReferenciaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where referencia= :nroReferencia");
			query.setString("nroReferencia", nroReferenciaIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	/** retorna un objeto insumo con todos sus atributos (sin adicionales) **/
	public static Insumo obtenerInsumoPorId1(Integer idInsumoIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where idInsumo= :id");
			query.setInteger("id", idInsumoIN);
			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	/** igual que el 1, solo q se le agrega el atributo adicional nombreCategoria **/
	public static Insumo obtenerInsumoPorId2(Integer idInsumoIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, cat, ins.nroLote, ins.nombreInsumo, "
					+ "ins.nroArticulo, ins.referencia, ins.fechaVencimiento, ins.temperatura, ins.precioInsumo, ins.unidadMedida, "
					+ "ins.estadoInsumo, ins.pdp, ins.stockActual, ins.nroPedido, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.categoria cat "
					+ "where ins.idInsumo= :id "
					+ "and ins.categoria = cat.pkIdCategoria");
			query.setInteger("id", idInsumoIN);
			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	public static Insumo obtenerInsumoStockGralPorIdCategoria(Integer idCategoriaIN, String nombreIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(sum(ins.stockActual)) "
					+ "from Insumo ins "
					+ "where ins.categoria = :idCategoria "
					+ "and ins.nombreInsumo = :nombre "
					+ "group by ins.nombreInsumo");
			query.setInteger("idCategoria", idCategoriaIN);
			query.setString("nombre", nombreIN);
			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	/**
	 * retorna insumo con dato extra (stockGral), teniendo en cuenta categoria y sucursal
	 */
	public static Insumo obtenerInsumoStockGralPorIdCategoriaYSucursal(Integer idCategoriaIN, String nombreIN, Integer idSucursalIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(sum(ins.stockActual)) "
					+ "from Insumo ins "
					+ "where ins.categoria = :idCategoria "
					+ "and ins.nombreInsumo = :nombre "
					+ "and ins.sucursal = :idSucursal "
					+ "group by ins.nombreInsumo");
			query.setInteger("idCategoria", idCategoriaIN);
			query.setString("nombre", nombreIN);
			query.setInteger("idSucursal", idSucursalIN);
			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	
	public static List<Insumo> obtenerListaInsumosActivosAgrupadosPorArticulo() {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, cat, ins.nroLote, ins.nombreInsumo, "
					+ "ins.nroArticulo, ins.referencia, ins.fechaVencimiento, ins.temperatura, ins.precioInsumo, ins.unidadMedida, "
					+ "ins.estadoInsumo, ins.pdp, sec.nombreSector, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where ins.estadoInsumo= :estado "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and cat.sector = sec.idSector "
					+ "group by ins.nroArticulo");
			query.setString("estado", "Activo");
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}

	public static List<Insumo> obtenerListaInsumosActivosAgrupadosPorNombreConStockGeneral() {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, cat, ins.nroLote, ins.nombreInsumo, "
					+ "ins.unidadMedida, ins.nroArticulo, ins.referencia, ins.precioInsumo, ins.nroPedido, "
					+ "sum(ins.stockActual), ins.pdp, ins.estadoInsumo, sec.nombreSector, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where ins.estadoInsumo= :estado "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and cat.sector = sec.idSector "
					+ "and ins.sucursal = 1 "
					+ "group by ins.nroArticulo");
			
			query1.setString("estado", "Activo");
			listaInsumos = query1.list();
			
			
			appMain.getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
			
			//rolling back to save the test data
			tx.rollback();
			
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	
	public static List<Insumo> obtenerListaInsumosPorNroLote(String nroLoteIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nroLote= :lote");
			query.setString("lote", nroLoteIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	public static List<Insumo> obtenerListaInsumosPorLoteArticuloYReferencia(String nroLoteIN, String nroArticuloIN, String referenciaIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nroLote= :lote "
					+ "and nroArticulo= :articulo "
					+ "and referencia= :ref");
			query.setString("lote", nroLoteIN);
			query.setString("articulo", nroArticuloIN);
			query.setString("ref", referenciaIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	public static Insumo obtenerInsumoPorNombreEIdCategoria(String nombreIN, Integer idCategoriaIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo = :nombre "
					+ "and categoria = :id");
			query.setString("nombre", nombreIN);
			query.setInteger("id", idCategoriaIN);
			insumo = (Insumo) query.setMaxResults(1).uniqueResult();
//			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	public static Insumo obtenerInsumoPorNombreIdCategoriaArticuloYReferencia(String nombreIN, Integer idCategoriaIN, String articuloIN, String referenciaIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo = :nombre "
					+ "and categoria = :id "
					+ "and nroArticulo= :articulo "
					+ "and referencia= :ref");
			query.setString("nombre", nombreIN);
			query.setInteger("id", idCategoriaIN);
			query.setString("articulo", articuloIN);
			query.setString("ref", referenciaIN);
			insumo = (Insumo) query.setMaxResults(1).uniqueResult();
//			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	/** retorna el id del insumo con el precio mas actual, busca todos los insumos q cumplan con las condiciones
	 * 		de igual nombre,articulo y referencia y de esa lista retorna el id del ultimo insumo de esa lista
	 * 		es decir, q retorna el ultimo insumo cargado.
	 * 		x lo tanto el q tiene el precio mas actual
	 */
	public static Integer obtenerIdInsumoPrecioActual(String nombreIN, String articuloIN, String referenciaIN) {
		Transaction tx = null;
		Integer idInsumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select max(ins.idInsumo) "
					+ "from Insumo ins "
					+ "where ins.nombreInsumo = :nombre "
					+ "and ins.nroArticulo= :articulo "
					+ "and ins.referencia= :ref");
			query.setString("nombre", nombreIN);
			query.setString("articulo", articuloIN);
			query.setString("ref", referenciaIN);
			idInsumo = (Integer) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return idInsumo;
	}
	
	
	/**
	 * obtiene id insumo teniendo en cuenta sucursal (diagnos = 1)
	 */
	public static Integer obtenerIdInsumoSucursalDiagnos(String nombreIN, String articuloIN, String referenciaIN) {
		Transaction tx = null;
		Integer idInsumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select max(ins.idInsumo) "
					+ "from Insumo ins "
					+ "where ins.nombreInsumo = :nombre "
					+ "and ins.nroArticulo= :articulo "
					+ "and ins.referencia= :ref "
					+ "and ins.sucursal = 1");
			query.setString("nombre", nombreIN);
			query.setString("articulo", articuloIN);
			query.setString("ref", referenciaIN);
			idInsumo = (Integer) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return idInsumo;
	}
	
	
	public static Insumo obtenerInsumoPorNombreCategoriaLoteArticuloYReferencia(String nombreInsumoIN, Integer idCategoriaIN,
																				String loteIN, String nroArticuloIN, String nroReferenciaIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo = :nombre "
					+ "and categoria = :id "
					+ "and nroLote = :lote "
					+ "and nroArticulo= :articulo "
					+ "and referencia= :ref");
			query.setString("nombre", nombreInsumoIN);
			query.setInteger("id", idCategoriaIN);
			query.setString("lote", loteIN);
			query.setString("articulo", nroArticuloIN);
			query.setString("ref", nroReferenciaIN);
			insumo = (Insumo) query.setMaxResults(1).uniqueResult();
//			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	
	/** este query hace lo mismo q el actualizarObjeto, solo de esta forma evitamos traer de la bd el insumo, para solamente actualizar
	 * 		un atributo, en este caso precio.
	 * 	directamente actualizamos el atributo sin necesidad de traer nada
	 */
//	public static void actualizarPrecioInsumoPorIdInsumo(Integer idInsumoIN, Float precioIN) {
//		Transaction tx = null;
//		try {
//			AppMain appMain = AppMain.getSingletonSession();
//			tx = appMain.getSession().beginTransaction();
//			
//			Query query = appMain.getSession().createQuery("update Insumo "
//					+ "set precioInsumo= :precio "
//					+ "where idInsumo= :id");
//			query.setParameter("precio", precioIN);
//			query.setInteger("id", idInsumoIN);
//			int result = query.executeUpdate();
//			
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			e.getMessage();
//		}
//	}
	
	
	//TRAE TODOS LOS INSUMOS CON SU PROVEEDOR
	/*
	 * USE bdcsd;
	 * SELECT ins.Id_Insumo, ins.Nombre_Insumo, ins.NroLote, pro.Nombre_Proveedor
	 * FROM insumo AS ins
	 * INNER JOIN proveedor AS pro ON ins.FK_Id_Proveedor = pro.PK_ID_Proveedor
	 * */
	public static List<Insumo> obtenerListaInsumosConProveedor() {
		Transaction tx = null;
		List<Insumo> listaInsumos = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, "
					+ "pro.pkIdProveedor, pro.nombreProveedor) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor pro "
					+ "where ins.proveedor = pro.pkIdProveedor");
			
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	//TRAE INSUMOS DE ACUERDO A UN ID PROVEEDOR
	/*
	 * USE bdcsd;
	 * SELECT ins.Id_Insumo, ins.Nombre_Insumo, ins.NroLote, ins.FK_ID_Proveedor, pro.Nombre_Proveedor
	 * FROM insumo AS ins
	 * INNER JOIN proveedor AS pro ON ins.FK_Id_Proveedor = pro.PK_ID_Proveedor
	 * WHERE ins.FK_Id_Proveedor = '10'
	 * */
	
	public static List<Insumo> obtenerListaInsumosPorIdProveedor(Integer idProveedorIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, "
					+ "ins.proveedor, prov.nombreProveedor) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor prov "
					+ "where ins.proveedor= :id ");
			query.setInteger("id", idProveedorIN);
			listaInsumos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	//TRAE UN INSUMO CON SU PROVEEDOR
	/*
	 * USE bdcsd;
	 * SELECT ins.Id_Insumo, ins.Nombre_Insumo, ins.NroLote, ins.FK_ID_Proveedor, pro.Nombre_Proveedor
	 * FROM insumo AS ins
	 * INNER JOIN proveedor AS pro ON ins.FK_Id_Proveedor = pro.PK_ID_Proveedor
	 * WHERE ins.Id_Insumo = '4'
	 * */
	
	public static Insumo obtenerInsumoPorIdProveedor(Integer idInsumo) {
		Insumo insumo = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, "
					+ "ins.proveedor, prov.nombreProveedor) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor prov "
					+ "where ins.idInsumo= :id ");
			query.setInteger("id", idInsumo);
			
			insumo = (Insumo) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}

//	 USE bdcsd;
//	 SELECT ins.Id_Insumo, ins.Nombre_Insumo, ins.Nro_Articulo, ins.NroLote, ins.FK_ID_Proveedor, pro.Nombre_Proveedor
//	 FROM insumo AS ins
//	 INNER JOIN proveedor AS pro ON ins.FK_Id_Proveedor = pro.PK_ID_Proveedor
//	 WHERE ins.FK_Id_Proveedor = '4'
//   GROUP BY ins.Nro_Articulo
	
	public static List<Insumo> obtenerListaInsumosActivosAgrupadosPorProveedorConStockGeneral(Integer idProveedorIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query1 = appMain.getSession().createQuery("select new Insumo(ins.idInsumo, ins.nombreInsumo, ins.nroArticulo, ins.referencia, ins.nroPedido, "
					+ "ins.proveedor, prov.nombreProveedor, cat, sec.nombreSector, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor prov "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where ins.proveedor= :id "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and cat.sector = sec.idSector "
					+ "group by ins.nroArticulo");
			
			query1.setInteger("id", idProveedorIN);
			listaInsumos = query1.list();
			
			
			appMain.getSession().close();

		} catch (Exception e) {
			e.printStackTrace();
			
			//rolling back to save the test data
			tx.rollback();
			
			e.getMessage();
		}
		return listaInsumos;
	}
	
	
	public static Insumo obtenerInsumoPorNombrePorNroLotePorIdCategoriaPorSucursal(String nombreInsumoIN, String nroLoteIN, Integer idCategoriaIN, Integer idSucursalIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo= :nombre "
					+ "and nroLote= :lote "
					+ "and categoria= :idCat "
					+ "and sucursal= :idSuc");
			query.setString("nombre", nombreInsumoIN);
			query.setString("lote", nroLoteIN);
			query.setInteger("idCat", idCategoriaIN);
			query.setInteger("idSuc", idSucursalIN);
			insumo = (Insumo) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	/** retorna pdp de insumo de acuerdo al nombre del insumo y el id sucursal **/
	public static Insumo obtenerPdpInsumoPorNombreInsumoYIdSucursal(String nombreInsumoIN, Integer idSucursalIN) {
		Transaction tx = null;
		Insumo insumo = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Insumo "
					+ "where nombreInsumo= :nombre "
					+ "and sucursal= :idSuc");
			query.setString("nombre", nombreInsumoIN);
			query.setInteger("idSuc", idSucursalIN);
			insumo = (Insumo) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return insumo;
	}
	
	
	/********************************** P R O V E E D O R ************************************/


	public static Proveedor obtenerProveedorPorId(Integer idProveedorIN) {
		Transaction tx = null;
		Proveedor proveedor = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Proveedor "
					+ "where pkIdProveedor= :id");
			query.setInteger("id", idProveedorIN);
			proveedor = (Proveedor) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return proveedor;
	}


	public static Proveedor obtenerProveedorPorNroCuit(String nroCuitIN) {
		Transaction tx = null;
		Proveedor proveedor = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Proveedor "
					+ "where nroCuit= :cuit");
			query.setString("cuit", nroCuitIN);
			proveedor = (Proveedor) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return proveedor;
	}
	
	
	public static List<Proveedor> obtenerListaProveedoresActivos() {
		List<Proveedor> listaProveedores = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Proveedor "
					+ "where estadoProveedor= :estado");
			query.setString("estado", "Activo");
			listaProveedores = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaProveedores;
	}
	
	
	public static Proveedor obtenerProveedorPorNombre(String nombreProveeIN) {
		Transaction tx = null;
		Proveedor proveedor = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Proveedor "
					+ "where nombreProveedor= :nombre");
			query.setString("nombre", nombreProveeIN);
			proveedor = (Proveedor) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return proveedor;
	}
	
	
	public static Proveedor obtenerProveedorDeUnDetalleRemitoPorIdRemito(Integer idRemitoIN) {
		Transaction tx = null;
		Proveedor proveedor = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Proveedor(p.nroCuit, p.nombreProveedor) "
					+ "from DetalleRemito dR "
					+ "inner join dR.proveedor p "
					+ "where dR.remito= :id "
					+ "and dR.proveedor = p.pkIdProveedor");
			query.setInteger("id", idRemitoIN);
			proveedor = (Proveedor) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return proveedor;
	}

	
	public static List<Insumo> obtenerListaInsumosStrockGralPdpPorIdProveedor(Integer idProveedorIN) {
		List<Insumo> listaInsumos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Insumo(ins.nombreInsumo, sum(ins.stockActual), ins.pdp, pro.nombreProveedor, "
					+ "sec.nombreSector, ins.nroArticulo , ins.referencia, ins.categoria, cat.nombreCategoria) "
					+ "from Insumo ins "
					+ "inner join ins.proveedor pro "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where ins.proveedor = :idProveedor "
					+ "and ins.sucursal = '1' "
					+ "group by ins.nombreInsumo "
					+ "having sum(ins.stockActual) <= ins.pdp");
			query.setInteger("idProveedor", idProveedorIN);
    		
    		listaInsumos = query.list();
    		
    		appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			//rolling back to save the test data
			tx.rollback();
			e.getMessage();
		}
		return listaInsumos;
	}
	
	/********************************** M O V I M I E N T O S ************************************/
	

	public static List<Movimiento> obtenerListaMovimientosPorFecha(Date fechaInicio, Date fechaFinal) {
		List<Movimiento> listaMovimientos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Movimiento(mov.insumo, mov.usuario, mov.nombreUsuario, "
					+ "mov.apellidoUsuario, mov.fechaMovimiento, mov.descripcion, ins.nroLote, ins.nombreInsumo, mov.sucursal) "
					+ "from Movimiento mov "
					+ "inner join mov.insumo ins "
					+ "where mov.fechaMovimiento >= \'" + fechaInicio + "\' and mov.fechaMovimiento <= \'" + fechaFinal + " 23:59:59 \' "
					+ "and mov.insumo = ins.idInsumo");
			
			listaMovimientos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaMovimientos;
	}

	
	/********************************** F A C T U R A ************************************/
	
	
	public static List<Factura> obtenerListaFacturas() {
		List<Factura> listaFacturas = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Factura(f.idFactura, f.nroFactura, f.fechaFactura, f.tipoFactura, "
					+ "f.fechaCarga, f.observacionFactura, f.tieneRemito, f.estadoFactura, p.pkIdProveedor, p.nombreProveedor, p.nroCuit, sum(dF.importe)) "
					+ "from DetalleFactura dF "
					+ "inner join dF.factura f "
					+ "inner join dF.proveedor p "
					+ "group by dF.factura");
			
			listaFacturas = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaFacturas;
	}


	public static Factura obtenerFacturaPorId(Integer idFacturaIN) {
		Transaction tx = null;
		Factura factura = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Factura "
					+ "where idFactura= :id");
			query.setInteger("id", idFacturaIN);
			factura = (Factura) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return factura;
	}
	
	
	public static Factura obtenerFacturaPorNroFactura(String nroFacturaIN) {
		Transaction tx = null;
		Factura factura = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Factura "
					+ "where nroFactura= :nro");
			query.setString("nro", nroFacturaIN);
			factura = (Factura) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return factura;
	}

	
	/********************************** D E T A L L E   F A C T U R A ************************************/

	public static List<DetalleFactura> obtenerListaDetalleFacturaPorIdFactura(Integer idFacturaIN) {
		List<DetalleFactura> listaDetalleFactura = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			/**el lef join, lo uso con remito xq ese campo en detalle factura puede ser null
			 * 	entonces evitamos que devuelva un query vacio
			 */
			Query query = appMain.getSession().createQuery("select new DetalleFactura(dF.factura, dF.insumo, dF.proveedor, "
					+ "r, dF.cantidad, dF.precio, dF.importe, "
					+ "f.nroFactura, r.nroRemito, ins.nombreInsumo, "
					+ "ins.nroLote, ins.nroArticulo, ins.unidadMedida, ins.fechaVencimiento, p.nroCuit) "
					+ "from DetalleFactura dF "
					+ "inner join dF.factura f "
					+ "inner join dF.insumo ins "
					+ "inner join dF.proveedor p "
					+ "left join dF.remito r "
					+ "where dF.factura = :id");
			query.setInteger("id", idFacturaIN);
			
			listaDetalleFactura = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaDetalleFactura;
	}
	
	
	/** tre todo lo de detalle factura, ademas de adicionales para detallefacturaFX y otros como
	 * 		idCategoria, nombreCategoria y nombreSector
	 */
	public static List<DetalleFactura> obtenerListaDetalleFacturaPorIdFactura2(Integer idFacturaIN) {
		List<DetalleFactura> listaDetalleFactura = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			/**el lef join, lo uso con remito xq ese campo en detalle factura puede ser null
			 * 	entonces evitamos que devuelva un query vacio
			 */
			Query query = appMain.getSession().createQuery("select new DetalleFactura(dF.id, dF.factura, dF.insumo, dF.proveedor, "
					+ "r, dF.cantidad, dF.precio, dF.importe, "
					+ "f.nroFactura, r.nroRemito, ins.nombreInsumo, "
					+ "ins.nroLote, ins.nroArticulo, ins.referencia, ins.unidadMedida, ins.fechaVencimiento, p.nroCuit, p.nombreProveedor, "
					+ "cat.pkIdCategoria, cat.nombreCategoria, sec.nombreSector) "
					+ "from DetalleFactura dF "
					+ "inner join dF.factura f "
					+ "inner join dF.insumo ins "
					+ "inner join dF.proveedor p "
					+ "left join dF.remito r "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where dF.factura = :id "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and cat.sector = sec.idSector");
			query.setInteger("id", idFacturaIN);
			
			listaDetalleFactura = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaDetalleFactura;
	}
	
	
	public static DetalleFactura obtenerDetalleFacturaPorNumFila(Integer numFilaIN) {
		DetalleFactura detalleFactura = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from DetalleFactura "
					+ "where id.pkNumDetalle = :fila");
			query.setInteger("fila", numFilaIN);
			
			detalleFactura = (DetalleFactura) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return detalleFactura;
	}
	
	
	public static DetalleFactura obtenerDetalleFacturaPorIdFactura(Integer idFacturaIN) {
		DetalleFactura detalleFactura = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from DetalleFactura "
					+ "where factura = :idFac");
			query.setInteger("idFac", idFacturaIN);
			
			detalleFactura = (DetalleFactura) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return detalleFactura;
	}
	
	
	/** lo mismo que el 1, solo q este ademas trae atributos adicionales de proveedor **/
	public static DetalleFactura obtenerDetalleFacturaPorIdFactura2(Integer idFacturaIN) {
		DetalleFactura detalleFactura = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new DetalleFactura(dF.factura, dF.insumo, dF.proveedor, "
					+ "r, dF.cantidad, dF.precio, dF.importe, "
					+ "f.nroFactura, r.nroRemito, ins.nombreInsumo, "
					+ "ins.nroLote, ins.nroArticulo, ins.unidadMedida, ins.fechaVencimiento, p.nroCuit, p.nombreProveedor) "
					+ "from DetalleFactura dF "
					+ "inner join dF.factura f "
					+ "inner join dF.insumo ins "
					+ "inner join dF.proveedor p "
					+ "left join dF.remito r "
					+ "where dF.factura = :idFac");
			query.setInteger("idFac", idFacturaIN);
			
			detalleFactura = (DetalleFactura) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return detalleFactura;
	}

	
	/********************************** R E M I T O ************************************/
	
	
	public static List<Remito> obtenerListaRemitos() {
		List<Remito> listaRemitos = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Remito(r.idRemito, f, r.nroRemito, r.fechaRemito, "
					+ "r.fechaCarga, r.observacionRemito, f.nroFactura, p.pkIdProveedor, p.nombreProveedor, p.nroCuit) "
					+ "from DetalleRemito dR "
					+ "inner join dR.remito r "
					+ "left join r.factura f "
					+ "inner join dR.proveedor p "
					+ "group by dR.remito");
			
			listaRemitos = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaRemitos;
	}
	
	
	public static Remito obtenerRemitoPorNroRemito(String nroRemitoIN) {
		List<Remito> listaRemito = null;
		Transaction tx = null;
		Remito remito = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Remito "
					+ "where nroRemito= :nro");
			query.setString("nro", nroRemitoIN);
			
//			remito = (Remito) query.uniqueResult();
			
			listaRemito = query.list();
			remito = listaRemito.get(0);
			
			appMain.getSession().close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		
		return remito;
	}
	
	
	public static Remito obtenerRemitoPorId(Integer idRemitoIN) {
		Transaction tx = null;
		Remito remito = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Remito "
					+ "where idRemito= :id");
			query.setInteger("id", idRemitoIN);
			remito = (Remito) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return remito;
	}
	
	
	/** igual q el 1, solo q este trae todos los atributos de forma manual en un select **/
	public static Remito obtenerRemitoPorId2(Integer idRemitoIN) {
		Transaction tx = null;
		Remito remito = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new Remito(r.idRemito, f, r.nroRemito, r.fechaRemito, "
					+ "r.fechaCarga, r.observacionRemito, f.nroFactura) "
					+ "from Remito r "
					+ "left join r.factura f "
					+ "where r.idRemito= :id");
			query.setInteger("id", idRemitoIN);
			remito = (Remito) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return remito;
	}
	
	
//	public static void actualizarFacturaEnRemitoPorIdRemito(Integer idRemitoIN, Factura facturaIN) {
//		Transaction tx = null;
//		try {
//			AppMain appMain = AppMain.getSingletonSession();
//			tx = appMain.getSession().beginTransaction();
//			
//			Query query = appMain.getSession().createQuery("update Remito "
//					+ "set factura= :fac "
//					+ "where idRemito= :id");
//			query.setParameter("fac", facturaIN);
//			query.setInteger("id", idRemitoIN);
//			int result = query.executeUpdate();
//			
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//			e.getMessage();
//		}
//	}
	
	
	/********************************** D E T A L L E   R E M I T O ************************************/
	
	/** este query retorna un detalle remito sin adicionales **/
	public static List<DetalleRemito> obtenerListaDetalleRemitoPorIdRemito(Integer idRemitoIN) {
		List<DetalleRemito> listaDetalleRemito = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from DetalleRemito "
					+ "where remito = :id");
			query.setInteger("id", idRemitoIN);
			
			listaDetalleRemito = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaDetalleRemito;
	}
	
	
	/** este query retorna un detalle remito necesario para la clase detalleremitofx **/
	public static List<DetalleRemito> obtenerListaDetalleRemitoPorIdRemito1(Integer idRemitoIN) {
		List<DetalleRemito> listaDetalleRemito = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new DetalleRemito(dR.insumo, dR.proveedor, dR.remito, dR.cantidad, "
					+ "ins.nroLote, p.nroCuit, r.nroRemito, ins.nombreInsumo, ins.nroArticulo, ins.unidadMedida, ins.fechaVencimiento) "
					+ "from DetalleRemito dR "
					+ "inner join dR.insumo ins "
					+ "inner join dR.proveedor p "
					+ "inner join dR.remito r "
					+ "where dR.remito = :id");
			query.setInteger("id", idRemitoIN);
			
			listaDetalleRemito = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaDetalleRemito;
	}
	
	
	/** este query ademas de traer todo lo q requiere la clase detalleremitofx, agrega atributos de categoria y sector **/
	public static List<DetalleRemito> obtenerListaDetalleRemitoPorIdRemito2(Integer idRemitoIN) {
		List<DetalleRemito> listaDetalleRemito = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new DetalleRemito(dR.insumo, dR.proveedor, dR.remito, dR.cantidad, "
					+ "ins.nroLote, p.nroCuit, r.nroRemito, ins.nombreInsumo, ins.nroArticulo, ins.unidadMedida, ins.fechaVencimiento, "
					+ "cat.pkIdCategoria, cat.nombreCategoria, sec.nombreSector, p.nroCuit, p.nombreProveedor) "
					+ "from DetalleRemito dR "
					+ "inner join dR.insumo ins "
					+ "inner join dR.proveedor p "
					+ "inner join dR.remito r "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where dR.remito = :id");
			query.setInteger("id", idRemitoIN);
			
			listaDetalleRemito = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaDetalleRemito;
	}
	
	
	/** este query retorna un detalle remito necesario para la clase detalleremitofx, y agrega atributos adicionales de proveedor **/
	public static List<DetalleRemito> obtenerListaDetalleRemitoPorIdRemito3(Integer idRemitoIN) {
		List<DetalleRemito> listaDetalleRemito = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new DetalleRemito(dR.insumo, dR.proveedor, dR.remito, dR.cantidad, "
					+ "ins.nroLote, p.nroCuit, p.nombreProveedor, r.nroRemito, ins.nombreInsumo, ins.nroArticulo, ins.unidadMedida, ins.fechaVencimiento) "
					+ "from DetalleRemito dR "
					+ "inner join dR.insumo ins "
					+ "inner join dR.proveedor p "
					+ "inner join dR.remito r "
					+ "where dR.remito = :id");
			query.setInteger("id", idRemitoIN);
			
			listaDetalleRemito = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaDetalleRemito;
	}


	public static DetalleRemito obtenerDetalleRemitoPorIdRemito(Integer idRemitoIN) {
		DetalleRemito detalleRemito = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from DetalleRemito "
					+ "where remito = :id");
			query.setInteger("id", idRemitoIN);
			
//			detalleRemito = (DetalleRemito) query.uniqueResult();
			
			detalleRemito = (DetalleRemito) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return detalleRemito;
	}


	/********************************** S U C U R S A L ************************************/

	public static List<Sucursal> obtenerListaSucursales() {
		List<Sucursal> listaSucursal = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sucursal ");
//			query.setString("estado", "Activo");
			listaSucursal = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaSucursal;
	}

	
	/** este query solo retorna la sucursal "DIAGNOS" **/
	public static Sucursal obtenerSucursal() {
		Sucursal sucursal = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sucursal "
					+ "where nombreSucursal = :nombre");
			query.setString("nombre", "Diagnos");
			
			sucursal = (Sucursal) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return sucursal;
	}
	
	
	public static Sucursal obtenerSucursalPorNombre(String nombreIN) {
		Sucursal sucursal = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from Sucursal "
					+ "where nombreSucursal = :nombre");
			query.setString("nombre", nombreIN);
			
			sucursal = (Sucursal) query.uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return sucursal;
	}

	/********************************** O R D E N   D E   C O M P R A  ************************************/


	public static List<OrdenDeCompra> obtenerListaOrdenesDeCompra() {
		List<OrdenDeCompra> listaOrdenesDeCompra = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from OrdenDeCompra ");
//			query.setString("estado", "Activo");
			listaOrdenesDeCompra = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaOrdenesDeCompra;
	}
	
	
	/** a diferencia del "obtenerListaOrdenesDeCompra()", este trae nombre de usuario y proveedor **/
	public static List<OrdenDeCompra> obtenerListaOrdenesDeCompra2() {
		List<OrdenDeCompra> listaOrdenesDeCompra = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new OrdenDeCompra(oC.pkIdOrdenCompra, u, p, oC.nroOrdenCompra, "
					+ "oC.fechaOrdenCompra, oC.observacionOrdenCompra, u.nombre, u.apellido, p.nombreProveedor) "
					+ "from OrdenDeCompra oC "
					+ "inner join oC.usuario u "
					+ "inner join oC.proveedor p");
			
			listaOrdenesDeCompra = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaOrdenesDeCompra;
	}
	

	public static OrdenDeCompra obtenerOrdenDeCompraPorNroOrden(String nroDeOrden) {
		Transaction tx = null;
		OrdenDeCompra ordenDeCompra = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("from OrdenDeCompra "
					+ "where Nro_OrdenCompra= :nroordendecompra");
			query.setString("nroordendecompra", nroDeOrden);
//			insumo = (Insumo) query.uniqueResult();
			ordenDeCompra = (OrdenDeCompra) query.setMaxResults(1).uniqueResult();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return ordenDeCompra;
	}
	
	public static Integer obtenerUltimoNroDeOrdenDeCompra() {
		Transaction tx = null;
		Integer nroOrden = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select max(odc.nroOrdenCompra) "
			+"from OrdenDeCompra odc");
			
			nroOrden = (Integer) query.uniqueResult();

			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return nroOrden;
	}
	
	
	public static List<OrdenDeCompra> obtenerListaOrdenDeCompraPorIdProveedor(Integer idProveedorIN) {
		List<OrdenDeCompra> listaOrdenesDeCompra = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new OrdenDeCompra(oC.pkIdOrdenCompra, u, p, oC.nroOrdenCompra, "
					+ "oC.fechaOrdenCompra, oC.observacionOrdenCompra, u.nombre, u.apellido, p.nombreProveedor) "
					+ "from OrdenDeCompra oC "
					+ "inner join oC.usuario u "
					+ "inner join oC.proveedor p "
					+ "where oC.proveedor = :id");
			
			query.setInteger("id", idProveedorIN);
			
			listaOrdenesDeCompra = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaOrdenesDeCompra;
	}
	
	
			/****************** D E T A L L E   O R D E N   D E   C O M P R A  ***********************/


	public static List<DetalleOrdenDeCompra> obtenerDetalleOrdenDeCompraPorIdOrden(Integer idOrdenCompraIN) {
		List<DetalleOrdenDeCompra> listaDetalleOrdenDeCompra = null;
		Transaction tx = null;
		try {
			AppMain appMain = AppMain.getSingletonSession();
			tx = appMain.getSession().beginTransaction();
			
			Query query = appMain.getSession().createQuery("select new DetalleOrdenDeCompra(dOc.id, dOc.insumo, dOc.ordenDeCompra, dOc.cantidad, "
					+ "dOc.observacion, dOc.precio, dOc.unidades, ins.nombreInsumo, ins.nroArticulo, ins.referencia, sec.nombreSector, cat.nombreCategoria, sec.idSector, "
					+ "cat.pkIdCategoria) "
					+ "from DetalleOrdenDeCompra dOc "
					+ "inner join dOc.insumo ins "
					+ "inner join ins.categoria cat "
					+ "inner join cat.sector sec "
					+ "where dOc.ordenDeCompra = :id "
					+ "and ins.categoria = cat.pkIdCategoria "
					+ "and cat.sector = sec.idSector");
			
			query.setInteger("id", idOrdenCompraIN);
			
			listaDetalleOrdenDeCompra = query.list();
			
			appMain.getSession().close();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			e.getMessage();
		}
		return listaDetalleOrdenDeCompra;
	}


//	public static Conexion LeeXML() {
//        org.w3c.dom.Document dom=null;
//        javax.xml.parsers.DocumentBuilderFactory dbf;
//        javax.xml.parsers.DocumentBuilder db;
//        dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
//        try
//        {
//          db = dbf.newDocumentBuilder();
//          dom = db.parse("src/main/resources/hibernate.cfg.xml");
//        }
//        catch(Exception ex) {}
//       
//        org.w3c.dom.Element rootElement = dom.getDocumentElement();
//        org.w3c.dom.NodeList nodeList = rootElement.getElementsByTagName("property");
////        System.out.println(nodeList.getLength());
//        Conexion c = new Conexion();
//        
//        if(nodeList != null && nodeList.getLength()>0) {
//          
//        	for(int i=0;i<nodeList.getLength();i++) {
//        		org.w3c.dom.Element element = (Element)nodeList.item(i);
//
//        		String attr = element.getAttribute("name");
//            
////                            System.out.println(element.getAttribute("name"));
//            
//        		if (attr.equals("hibernate.connection.url")) {
//        			c.setUrl(element.getFirstChild().getNodeValue());
//        			break;
//        		}
//            
//        	}
//        }
//        return c;
//	}
	
}
