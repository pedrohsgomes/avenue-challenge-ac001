/**
 * 
 */
package com.avenuecode.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author pedro.gomes 2018-08-08 10:08:44
 *
 */
public class GraphInfoResponse  implements Serializable {

	private static final long serialVersionUID = -7058409564530864752L;
	private List<GraphDTO> data;
	public List<GraphDTO> getData() {
		return data;
	}
	public void setData(List<GraphDTO> data) {
		this.data = data;
	}

}
