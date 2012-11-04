function fQ = FundamentalFrequency(lpmm, mm)
%   lp/mm       6.25 	    3.67 	    0.63
%   dpi         2400 	    2400 	    2400
%   1 spot 	  0.0106 	  0.0106 	  0.0106
%   mm/lp     0.1600 	  0.2725 	  1.5873
%   px/lp    15.1181 	 25.7461 	149.9813  
%  --------------------------------------
%   fQ         66.25	  38.902	   6.678

  fQ	= 2*mm*lpmm;
  
end
