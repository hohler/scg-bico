package tool.bico.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ch.unibe.scg.metrics.sourcemetrics.domain.SMFile;

@Entity
@Table(name = "sourcemetrics")
public class SourceMetric {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	protected Commit commit;
	
	private String file;
	private String className;
	private String type;

	private int dit;
	private int noc;
	private int wmc;
	private int cbo;
	private int lcom;
	private int rfc;
	private int nom;
	private int nopm;
	private int nosm;

	private int nof;
	private int nopf;
	private int nosf;

	private int nosi;
	private int loc;
	
	private int nocb; // number of catch blocks
	private int nonc; // number of null checks
	private int nona; // number of null assignments
	private int nomwmop; // number of methods with more than 1 parameter	public SourceMetric() {}
	
	public SourceMetric() {}
	
	public SourceMetric(SMFile f) {
		file = f.getFile();
		className = f.getClassName();
		type = f.getType();
		cbo = f.getCbo();
		wmc = f.getWmc();
		dit = f.getDit();
		noc = f.getNoc();
		rfc = f.getRfc();
		lcom = f.getLcom();
		nom = f.getNom();
		nopm = f.getNopm(); 
		nosm = f.getNosm();
		nof = f.getNof();
		nopf = f.getNopf(); 
		nosf = f.getNosf();
		nosi = f.getNosi();
		loc = f.getLoc();
		nocb = f.getNocb();
		nonc = f.getNonc();
		nona = f.getNona();
		nomwmop = f.getNomwmop();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDit() {
		return dit;
	}

	public void setDit(int dit) {
		this.dit = dit;
	}

	public int getNoc() {
		return noc;
	}

	public void setNoc(int noc) {
		this.noc = noc;
	}

	public int getWmc() {
		return wmc;
	}

	public void setWmc(int wmc) {
		this.wmc = wmc;
	}

	public int getCbo() {
		return cbo;
	}

	public void setCbo(int cbo) {
		this.cbo = cbo;
	}

	public int getLcom() {
		return lcom;
	}

	public void setLcom(int lcom) {
		this.lcom = lcom;
	}

	public int getRfc() {
		return rfc;
	}

	public void setRfc(int rfc) {
		this.rfc = rfc;
	}

	public int getNom() {
		return nom;
	}

	public void setNom(int nom) {
		this.nom = nom;
	}

	public int getNopm() {
		return nopm;
	}

	public void setNopm(int nopm) {
		this.nopm = nopm;
	}

	public int getNosm() {
		return nosm;
	}

	public void setNosm(int nosm) {
		this.nosm = nosm;
	}

	public int getNof() {
		return nof;
	}

	public void setNof(int nof) {
		this.nof = nof;
	}

	public int getNopf() {
		return nopf;
	}

	public void setNopf(int nopf) {
		this.nopf = nopf;
	}

	public int getNosf() {
		return nosf;
	}

	public void setNosf(int nosf) {
		this.nosf = nosf;
	}

	public int getNosi() {
		return nosi;
	}

	public void setNosi(int nosi) {
		this.nosi = nosi;
	}

	public int getLoc() {
		return loc;
	}

	public void setLoc(int loc) {
		this.loc = loc;
	}
	
	public int getNocb() {
		return nocb;
	}

	public void setNocb(int nocb) {
		this.nocb = nocb;
	}

	public int getNonc() {
		return nonc;
	}

	public void setNonc(int nonc) {
		this.nonc = nonc;
	}

	public int getNona() {
		return nona;
	}

	public void setNona(int nona) {
		this.nona = nona;
	}

	public int getNomwmop() {
		return nomwmop;
	}

	public void setNomwmop(int nomwmop) {
		this.nomwmop = nomwmop;
	}

	public String getShortFile() {
		if(file == null) return "";
		String[] fileName = file.split("/");
		return (fileName.length-3 > 0 ? fileName[fileName.length-3] + "/": "") + (fileName.length-2 > 0 ? fileName[fileName.length-2] + "/" : "") + fileName[fileName.length-1];
	}
	
	public String toString() {
		return "SourceMetric ["+file+", cbo: "+cbo+", dit: "+dit+", noc: " + noc + ", nof: " + nof + ", nopf: " + nopf + ", nosf: " + nosf + ", nom: " + nom + ", nopm: " + nopm + ", nosm: " + nosm
				+ ", nosi: " + nosi + ", rfc: " + rfc + ", wmc: " + wmc + ", loc: " + loc + ", lcom: " + lcom + ", nocb: " + nocb + ", nonc: " + nonc + ", nona: " + nona + ", nomwmop: " + nomwmop;
	}
}
