function [pth exists folder] = GetResourcePath(type, id, ext)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  seriesFolder = 'Series101';
  
  if exist('id', 'var')
    if isstruct(id)
      id = PatchSeriesProcessor.GetParameterID(id, type);
    end
  else
    id = [];
  end
  
  if exist('ext', 'var') && ~isempty(id);
    ext = ['.' regexprep(ext,'\W','')];
  else
    ext = [];
  end
  
  groupFolder       = regexpi(type, '\<(screen|halftone|contone|monotone)\>', 'match');
  if numel(groupFolder)==1
    groupFolder = groupFolder{1};
    groupFolder(1)  = upper(groupFolder(1));
  else
    groupFolder     = []; %'Others';
  end
   
  subFolder         = regexpi(type, '\<(image|fftdata|fftimage|retinaimage|retinafftimage|data|output|report)\>', 'match');
  
  if numel(subFolder)==1
    subFolder       = subFolder{1};
    subFolder(1)    = upper(subFolder(1));
  else
    subFolder       = [];
  end
  
  switch lower(subFolder)
    case {'image', 'report', 'retinaimage'} %, 'fftimage', 'retinafftimage'}
      parentFolder  = [];
    otherwise
      parentFolder  = 'Resources';
  end
  
  folder            = fullfile('Output', seriesFolder, parentFolder, groupFolder, subFolder);

  pth               = fullfile(folder, [id ext]);
  
  if ~isempty(folder) && exist(folder, 'dir')~=7
    FS.mkDir(folder);
  end
  
  if nargout>1
    if ~isempty(id)
      exists        = exist(pth, 'file')>0;
    else
      exists        = exist(pth, 'dir')>0;
    end
  end
end
