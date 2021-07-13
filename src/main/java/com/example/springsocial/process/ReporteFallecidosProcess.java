package com.example.springsocial.process;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

import com.example.springsocial.crud.ModelSetGetTransaction;
import com.example.springsocial.crud.ObjectSetGet;
import com.example.springsocial.repository.CabeceraFolioRepositoryN;
import com.example.springsocial.repository.DetalleFolioHistoricoRepository;
import com.example.springsocial.repository.DetalleFolioRepositoryN;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.tools.DateTools;
import com.example.springsocial.tools.RestResponse;
import com.example.springsocial.tools.SearchCriteriaTools;

@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ReporteFallecidosProcess {

	
	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;
	private EntityTransaction transaction  = null;
	private EntityManager entityManager = null;
	//REPOSITORY
	private DetalleFolioRepositoryN rpDetalle = null;
	private CabeceraFolioRepositoryN rpCabeceraFolio = null;
	private DetalleFolioHistoricoRepository rpDetalleHistorico = null;
	//TOOLS
	private SearchCriteriaTools searchCriteriaTools= new SearchCriteriaTools();
	private ModelSetGetTransaction modelTransaction =new ModelSetGetTransaction();
	private DateTools dateTools = new DateTools();
	private ObjectSetGet data= new ObjectSetGet();
	private UserPrincipal userPrincipal =null;
	private RestResponse response= new RestResponse();
	
	
	public RestResponse getResponse() {return this.response; }
	public void setData(Object createElement) {data.setObject(createElement);}
	public void setUserPrincipal(UserPrincipal userPrincipal) {this.userPrincipal=userPrincipal;}
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {if(entityManagerFactory!=null) this.entityManagerFactory=entityManagerFactory;}
	public void setRepository(DetalleFolioRepositoryN rpDetalle, CabeceraFolioRepositoryN rpCabeceraFolio, DetalleFolioHistoricoRepository rpDetalleHistorico) 
	{this.rpDetalle = rpDetalle; this.rpCabeceraFolio=rpCabeceraFolio; this.rpDetalleHistorico=rpDetalleHistorico;}
	
	
	private void startTransaction() {
		this.entityManager = this.entityManagerFactory.createEntityManager();
		this.transaction = this.entityManager.getTransaction();
		this.transaction.begin();
		this.modelTransaction.setEntityManager(entityManager);
	}
	
	private void confirmTransactionAndSetResult() {
		transaction.commit();
	}
	
	private void busquedaReporte() {
		
		try {
			
			switch(Integer.valueOf(data.getValue("criterio").toString())) {
			case 1:
				//REPORTE GENERAL DEL MES
				
			break;
			case 2:
			break;
			case 3:
			break;
			case 4:
			break;
			case 5:
			break;
			default:
				
			}
			
		}catch(Exception e) {
			
		}
	}
	
}
