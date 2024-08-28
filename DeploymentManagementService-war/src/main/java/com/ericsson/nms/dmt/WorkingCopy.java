package com.ericsson.nms.dmt;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.ericsson.nms.dmt.rest.hateoas.LinkableResource;

/**
 * Entity class used to persist working copies' related information. If an
 * instance of {@link WorkingCopy} has LOCAL mode, the attribute
 * 'modelDefinition' will contain the structure of the deployment model in XML
 * format. If it has LIVE mode, the deployment model definition is persisted and
 * managed by a remote LITP server and, hence, will not be found on its
 * 'modelDefinition' attribute. Only one LIVE working copy will exist per DMT
 * installation. On the other hand, users can create as many LOCAL working
 * copies as they want.
 */
@Entity
@Table(name = "WORKING_COPY", schema = "DMT")
@NamedQueries(value = { @NamedQuery(name = WorkingCopy.FIND_ALL, query = "SELECT o FROM WorkingCopy o ORDER BY o.name ASC") })
public class WorkingCopy extends LinkableResource implements Serializable {

	private static final long serialVersionUID = 5148642002365742426L;

	public static final String FIND_ALL = "findAll";

	@Id
	@Column(nullable = false)
	private String id;

	@Column(nullable = false)
	private String name;

	private String description;

	@Column(name = "DESIGN_MODE", nullable = false)
	@Enumerated(STRING)
	private Mode mode;

	@Lob
	@Column(name = "MODEL_DEFINITION")
	@JsonIgnore
	private String modelDefinition;

	@Column(nullable = false)
	@Temporal(TIMESTAMP)
	private Date created;

	@Column(name = "LAST_MODIFIED", nullable = false)
	@Temporal(TIMESTAMP)
	private Date lastModified;

	@Version
	@JsonIgnore
	private Long version;

	/**
	 * Default constructor
	 */
	public WorkingCopy() {
	}

	/**
	 * Returns the working copy identifier
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the working copy name. The name is a short description provided
	 * by the user that helps him/her to identify it
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the working copy name. Unlike the name attribute, it is a long
	 * description provided by the user that provides further information about
	 * the working copy
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns whether the current working copy refers to a local one (stored in
	 * DMT) or a live one (managed by a LITP server)
	 * 
	 * @return
	 */
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	/**
	 * If it is a LOCAL working copy, returns the deployment model serialized in
	 * XML format. Otherwise returns null
	 * 
	 * @return
	 */
	public String getModelDefinition() {
		return modelDefinition;
	}

	public void setModelDefinition(String definition) {
		this.modelDefinition = definition;
	}

	/**
	 * Informs when the working copy was created
	 * 
	 * @return
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * Forces the entity to refresh the last modified date and to synchronize it
	 * to the database
	 */
	public void refreshLastModified() {
		lastModified = new Date();
	}

	/**
	 * Informs when the working copy was last modified
	 * 
	 * @return
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * JPA callback called before an INSERT statement
	 */
	@PrePersist
	private void beforeCreate() {
		id = UUID.randomUUID().toString();
		created = new Date();
		refreshLastModified();
	}

	/**
	 * JPA callback called before an UPDATE statement
	 */
	@PreUpdate
	private void beforeUpdate() {
		refreshLastModified();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkingCopy other = (WorkingCopy) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
