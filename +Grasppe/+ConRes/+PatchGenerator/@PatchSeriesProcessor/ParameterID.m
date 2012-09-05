function str = ParameterID(obj, parameters, type)
  
  import Grasppe.ConRes.PatchGenerator.Processors.*;
  
  %% Patch Abbreviations
  
  if ~exist('type', 'var'), type = 'normal'; end
  
  switch lower(type)
    case {'screen', 'scr', 's'}
      codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
      codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
      % codes.Screen.(Screen.PPI)       = {'PPI', 4,  4,  1,    []  };
      codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
      codes.Screen.(Screen.LPI)       = {'LPI', 3,  3,  1,    []  };
      codes.Screen.(Screen.ANGLE)     = {'DEG', 3,  3,  10,   []  };
      codes.Screen.(Screen.TVI)       = {'TVI', 2,  2,  1,    0   };
      codes.Screen.(Screen.NOISE)     = {'NPV', 2,  2,  10,   0   };
      codes.Screen.(Screen.RADIUS)    = {'BPX', 2,  2,  1,    0   };
      codes.Screen.(Screen.BLUR)      = {'BPV', 3,  3,  1,    0   };
      codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
      codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
    case {'contone', 'cont', 'con', 'c'}
      codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
      codes.Patch.(Patch.CONTRAST)    = {'CON', 3,  3,  10,   []  };
      codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
      codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
      codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
      codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
      codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
    case {'monotone', 'mono', 'm'}
      codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
      codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
      codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
      codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
      codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
    otherwise
      codes.Patch.(Patch.MEANTONE)    = {'RTV', 2,  3,  1,    []  };
      codes.Patch.(Patch.CONTRAST)    = {'CON', 3,  3,  10,   []  };
      codes.Patch.(Patch.RESOLUTION)  = {'RES', 3,  3,  100,  []  };
      codes.Patch.(Patch.SIZE)        = {'MM',  3,  3,  100,  []  };
      
      %% Screen codes
      % codes.Screen.(Screen.PPI)       = {'PPI', 4,  4,  1,    []  };
      codes.Screen.(Screen.SPI)       = {'SPI', 4,  4,  1,    []  };
      codes.Screen.(Screen.LPI)       = {'LPI', 3,  3,  1,    []  };
      codes.Screen.(Screen.ANGLE)     = {'DEG', 3,  3,  10,   []  };
      
      %% Print codes
      codes.Screen.(Screen.TVI)       = {'TVI', 2,  2,  1,    0   };
      codes.Screen.(Screen.NOISE)     = {'NPV', 2,  2,  10,   0   };
      codes.Screen.(Screen.RADIUS)    = {'BPX', 2,  2,  1,    0   };
      codes.Screen.(Screen.BLUR)      = {'BPV', 3,  3,  1,    0   };
      
      %% Scan codes
      codes.Scan.(Scan.DPI)           = {'DPI', 4,  4,  1,    []  };
      codes.Scan.(Scan.SCALE)         = {'X',   3,  4,  1,    100 };
  end
  
  obj.Codes                       = codes;
  
  
  parameterGroups = fieldnames(parameters);
  
  str = '';
  for m = 1:numel(parameterGroups)
    groupName           = parameterGroups{m};
    groupParameters     = parameters.(groupName);
    groupString         = [groupName '{'];
    try
      groupFields       = fieldnames(groupParameters);
      for n = 1:numel(groupFields)
        try
          fieldName     = groupFields{n};
          fieldValue    = parameters.(groupName).(fieldName);
          
          fieldCode     = codes.(groupName).(fieldName);
          fieldLabel    = fieldCode{1};
          fieldSize     = [fieldCode{2:3}];
          fieldFactor   = fieldCode{4};
          fieldDefault  = fieldCode{5};
          
          if isequal(fieldDefault, fieldValue), continue; end
          
          if isnumeric(fieldValue)
            if isscalar(fieldFactor), fieldValue  = fieldValue.*fieldFactor; end
            fieldString     = sprintf('%.0f', fieldValue);
            % fieldString   = int2str(fieldValue); %strrep(num2str(fieldValue, ['%0' int2str(fieldSize(1)) '.0f' ]), '.', '');  %'%03.2f'),
            
            while numel(fieldString)<fieldSize(1)
              fieldString = ['0' fieldString];
            end
            
          elseif ~ischar(fieldValue)
            fieldString   = toString(fieldValue);
          end
          
          valueLength     = numel(fieldString);
          
          if isnumeric(fieldSize)
            fieldString     = fieldString(1:min(fieldSize(2), max(fieldSize(1), valueLength)));
          end
          
          str             = [str '-' fieldLabel fieldString]; %'-'
          % end
        end
      end
    end
  end
  
  str = str(2:end);
  
end
