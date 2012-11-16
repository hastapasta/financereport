package com.pikefin;

public class TheKey {

		
		 public final Integer key1;
		 public final Integer key2;
		 
		 public TheKey(Integer strVal1, Integer strVal2) {
			    this.key1 = strVal1; 
			    this.key2 = strVal2; //this.k3 = k3; this.k4 = k4;
			  }
		 
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			int outerhashcode = 7;
			//result = prime * result + getOuterType().hashCode();
			result = prime * result + outerhashcode;
			result = prime * result
					+ ((key1 == null) ? 0 : key1.hashCode());
			result = prime * result
					+ ((key2 == null) ? 0 : key2.hashCode());
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
			TheKey other = (TheKey) obj;
			/*if (!getOuterType().equals(other.getOuterType()))
				return false;*/
			if (key1 == null) {
				if (other.key1 != null)
					return false;
			} else if (!key1.equals(other.key1))
				return false;
			if (key2 == null) {
				if (other.key2 != null)
					return false;
			} else if (!key2.equals(other.key2))
				return false;
			return true;
		}
		/*private TheKey getOuterType() {
			return this;
		}*/
	
}
