function [B W] = FrequencyRange(diameter, ppi)
  
  W         = 3;  
  B         = diameter/7;
  
  try
    dpmm    = ppi/25.4;
    %[Lp Lm] = Grasppe.ConRes.Math.VisualResolution(ppi);
    %W       = Lp*1.5;
    B       = diameter*dpmm/3;
  end

end
