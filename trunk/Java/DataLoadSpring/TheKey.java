public class TheKey {
		
		 public final String strVal1;
		 public final String strVal2;
		 
		 public TheKey(String strVal1, String strVal2) {
			    this.strVal1 = strVal1; this.strVal2 = strVal2; //this.k3 = k3; this.k4 = k4;
			  }
		 
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			int outerhashcode = 7;
			//result = prime * result + getOuterType().hashCode();
			result = prime * result + outerhashcode;
			result = prime * result
					+ ((strVal1 == null) ? 0 : strVal1.hashCode());
			result = prime * result
					+ ((strVal2 == null) ? 0 : strVal2.hashCode());
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
			if (strVal1 == null) {
				if (other.strVal1 != null)
					return false;
			} else if (!strVal1.equals(other.strVal1))
				return false;
			if (strVal2 == null) {
				if (other.strVal2 != null)
					return false;
			} else if (!strVal2.equals(other.strVal2))
				return false;
			return true;
		}
		private TheKey getOuterType() {
			return this;
		}
	}
