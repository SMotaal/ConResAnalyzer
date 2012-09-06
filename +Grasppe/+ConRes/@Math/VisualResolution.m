function [Lp Lm] = VisualResolution(ppi, va, vd)
  persistent PPI LM LP VA VD;
  
  if ~exist('va', 'var'), va = 1/60 * pi/180; end
  if ~exist('vd', 'var'), vd = 30*10;         end
  
  if isequal(PPI, ppi) && isequal(VA, va) && isequal(VD, vd) ...
      && isscalar(LM) && isscalar(LP)
    Lp              = LP;
    Lm              = LM;
    return;
  end
  
  PPI               = ppi;
  dpmm              = PPI/25.4;
  VD                = vd;                 % mm
  VA                = va;                 % 1/60 * pi/180;
  LM                = VD*(tan(VA/2));     % mm resolution
  LP                = dpmm*LM;          % px resolution
  
  Lp                = LP;
  Lm                = LM; 
  
end
