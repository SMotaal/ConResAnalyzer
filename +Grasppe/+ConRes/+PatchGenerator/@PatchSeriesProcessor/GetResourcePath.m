function [pth exists folder] = GetResourcePath(type, id, ext, local)
  
  import(eval(NS.CLASS)); % PatchSeriesProcessor
  
  %   persistent ResourceFolders;
  %   if isempty(ResourceFolders), ResourceFolders = getResourceFolders; end
  try
    
    seriesFolder = PatchSeriesProcessor.SeriesID; 'Series103a'; %'Series101';
    
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
    
    %% Determine group folder    
    groupFolder       = regexpi(type, '\<(screen|halftone|contone|monotone|patch)\>', 'match');
    if numel(groupFolder)==1
      groupFolder = groupFolder{1};
      groupFolder(1)  = upper(groupFolder(1));
    else
      groupFolder     = []; %'Others';
    end
    
    % Determine sub folder    
    subFolder         = regexpi(type, [ ...
      '\<(image|fftdata|fftimage|retinaimage|retinafftimage|blockimage|blockcomp|' ...
      'data|output|report|compositeimage|hybridimage|srfplot|prfplot)\>'], 'match');
    
    if numel(subFolder)==1
      subFolder       = subFolder{1};
      subFolder(1)    = upper(subFolder(1));
    else
      try
        subFolder     = subFolder{1};
      catch err
        subFolder     = [];
      end
    end
    
    %% Remove additional parameters
    try
      if isempty(strfind(lower(subFolder), 'block'))
        switch lower(groupFolder)
          case {'contone'}
            id = regexprep(id, '(LPI|DEG)\d+', '');
          case {'monotone'}
            id = regexprep(id, '(RTV|CON|LPI|DEG)\d+', '');
          case {'screen'}
        end
      end
      
      id = regexprep(id, '(^-*|(?<=-)-*|-*$)', ''); % '-+', '-');
    end
    
    %% Determine parent folder
    switch lower(subFolder)
      case {'image', 'report', 'retinaimage'} %, 'fftimage', 'retinafftimage'}        
        parentFolder  = [];        
      otherwise
        parentFolder  = 'Resources';
    end
    
%     if ~exist('local', 'var')
%       local = false;
%     else
%       local = isequal(local, true);
%     end

    local             = exist('local', 'var')>0 && isequal(local, true);
        
    %folder            = fullfile(getOutputPath(seriesFolder), parentFolder, groupFolder, subFolder);
    folder            = findOutputPath(seriesFolder, parentFolder, groupFolder, subFolder, id,  ext, local);
    
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
    
  catch err
    debugStamp();
  end
end

function folder = findOutputPath(seriesFolder, parentFolder, groupFolder, subFolder, id,  ext, local)
  paths       = {...
    {'/Volumes', 'daflairsStoreQuattro', 'Output'}, ... %{'.','Output'}, ...{'/Volumes', 'daflairsStore 3.0', 'Output'}, ...
    };
  
  localPath   = {'.','Output'};
  
  if isequal(local, true)
    paths     = {paths{:}, localPath};
  else
    paths     = {localPath, paths{:}};
  end

  for m = 1:numel(paths)
    folder = fullfile(paths{m}{:}, seriesFolder, parentFolder, groupFolder, subFolder);
    if exist(fullfile(folder, [id ext]), 'file') > 0
      return; %newfolders{end+1} = folder;
    end
  end
  
end

function folder = getOutputPath(seriesFolder)
  paths       = {...
    {'/Volumes', 'daflairsStoreQuattro', 'Output'}, ... %{'/Volumes', 'daflairsStore 3.0', 'Output'}, ...
    {'.','Output'}, ...
    };
  
  
  for m = 1:numel(paths)
    folder = fullfile(paths{m}{:}, seriesFolder);
    if isdir(folder) %exist(folder, 'dir') > 0
      return; %newfolders{end+1} = folder;
    end
  end
  
  folder = fullfile(paths{1}{:}, seriesFolder);
end


% function folders = getResourceFolders(folders, seriesFolder)
%
%   persistent Folders;
%
%   if ischar(folders)
%     switch lower(folders)
%       case 'clear'
%         Folders = [];
%         folders = [];
%     end
%   end
%
%   if isempty(folders) || isempty(Folders)
%
%     paths       = {{'.','Ouput'}};
%
%     newfolders  = {};
%
%     %% Paths
%     for m = 1:numel(paths)
%       folder = fullfile(paths{m}{:}, seriesFolder);
%       if isdir(folder) %exist(folder, 'dir') > 0
%         newfolders{end+1} = folder;
%       end
%     end
%
%     volumes = getVolumes();
%
%     %% Volumes
%     for m = 1:numel(volumes)
%       folder = fullfile(volumes{m}, 'Ouput', seriesFolder);
%       if isdir(folder) %exist(folder, 'dir') > 0
%         newfolders{end+1} = folder;
%       end
%     end
%
%     Folders = newfolders;
%
%   end
%
%   folders = Folders;
%
% end
%
% function volumes = getVolumes()
%   volumes     = {};
%   if ismac
%     volumes   = dir('/Volumes');
%     volumes   = volumes(cellfun(@(x)x(1)~='.', {volumes(:).name})); % & cellfun(@(x)x==1, {s(:).isdir}))
%     volumes   = {['/Volumes/' volumes(:).name]};
%   elseif ispc
%     for d = 'a':'z'
%       if isdir([d ':']); volumes{end+1} = [d ':']; end
%     end
%   end
% end
