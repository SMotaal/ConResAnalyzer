classdef ConRes
  %DEFAULTS Summary of this class goes here
  %   Detailed explanation goes here
  
  properties
    IMPORTS = {'Grasppe.Patterns.*' , 'Grasppe.Kit.*'};
    
    TONE_RANGE        = 5:5:95;
    CONTRAST_RANGE    = [100 70.17 49.239 34.551 24.245 17.013 11.938 8.377 5.878 4.125 2.894 2.031 1.425 1.0];
    % Grasppe.Kit.LogSeries(1, 100, 14);
    % [100 68.1 46.4 31.6 21.5 14.7 10.0 6.8 4.6 3.2 2.2 1.5 1.0];
    RESOLUTION_RANGE  = [0.625	0.746	0.891	1.064	1.269	1.515	1.812	2.155	2.577	3.086	3.676	4.386	5.208	6.25];
    % Grasppe.Kit.LogSeries(a, b, c);     % [0.63	0.75	0.89	1.06	1.27	1.52	1.81	2.16	2.58	3.09	3.68	4.39	5.21	6.25];
    
    DEFAULT_PATCH_WIDTH     = 5.3;  % mm
    DEFAULT_ADDRESSIBILITY  = 2450; % spi
    DEFAULT_RESOLUTION      = 0.63; % lppmm
  end
  
  methods (Access=private)
    
    function obj = ConRes()
    end
    
  end
  
  methods (Static)
    
    function [image spec] = GeneratePatchImage(reference, contrast, resolution, width, addressibility)
           
      Grasppe.Kit.ConRes.GetImports;...
        import(Imports{:}); ...
        ConRes.GetInstance;
      
      if ~exist('reference', 'var') || isempty(reference)
        reference = 50;
      end
      
      if ~exist('contrast', 'var') || isempty(contrast)
        contrast = 100;
      end      
      if ~exist('width', 'var') || isempty(width)
        width = Instance.DEFAULT_PATCH_WIDTH; % mm
      end
      
      if ~exist('addressibility', 'var') || isempty(width)
        addressibility = Instance.DEFAULT_ADDRESSIBILITY;
      end
      
      if ~exist('resolution') || isempty(width)
        resolution = Instance.DEFAULT_RESOLUTION; % lppmm
      end
           
      cpmm    = resolution;
      cpin    = cpmm * 25.4;
      cppx    = cpin / addressibility;
      
      cycles  = width * cpmm;
      pixels  = cycles / cppx;
      

      [image rg ct rtv]   = ConcentricCircles(cppx*100, reference, contrast, ceil(pixels));
      
      spec = [100*(1-rtv) 100*ct resolution];

      %image = []; %Grasppe.Patterns.ConcentricCircles(cycles, );
    end
    
  end
  
  
  methods (Static)  % Getters
    function value = ContrastRange()
      Grasppe.Kit.ConRes.GetInstance;
      value     = Instance.CONTRAST_RANGE;
    end
    
    function value = ResolutionRange()
      Grasppe.Kit.ConRes.GetInstance;
      value     = Instance.RESOLUTION_RANGE;
    end 
    
    function value = ToneRange()
      Grasppe.Kit.ConRes.GetInstance;
      value     = Instance.TONE_RANGE;
    end    
    
    function rexp = LogSeries(a, b, c)
      
      rexp = Grasppe.Kit.LogSeries(a, b, c);
      %r = b/a; rlog = log(r)/(c-1); r2 = [0:c-1] * rlog; rexp = exp(r2);
    end
    
    function [instance class] = GetInstance()
      persistent Instance; ...
        Class = eval(CLASS);
      
      Instance = eval(Grasppe.Kit.GetInstance); ...
        instance = Instance; ...
        class    = Class;
      
      if nargout == 0
        assignin('caller', 'Instance', Instance);
      end
    end
    
    function [imports] = GetImports
      eval([eval(CLASS) '.GetInstance;']);
      
      imports = Instance.IMPORTS;
      
      assignin('caller', 'Imports', imports);
      
      % for m = 1:numel(imports)
      %   evalin('caller', ['import(''' imports{m} ''');']);
      % end
      % evalin('caller', 'import(''Imports{:}'');');
    end
    
  end    
    
  
end

