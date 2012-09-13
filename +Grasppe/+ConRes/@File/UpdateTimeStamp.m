function UpdateTimeStamp( pth )
  %UPDATETIMESTAMP Summary of this function goes here
  %   Detailed explanation goes here
  
  [fld fn ext] = fileparts(pth);
  
  m = 1;
  try
    while m<4 && isdir(fld) && ~(isequal('output', lower(fld)) && isempty(ext))
      system(['touch ' fld]);
      [fld fn ext] = fileparts(fld);
      m = m +1;
    end
  end
  
end

