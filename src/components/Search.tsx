import { searchIcon } from "../assets/icons";


const Search = ({ placeholder, query, setQuery }
  : {
    placeholder: string,
    query: string,
    setQuery: any
  }) => {
  return (
    <div className="font-inter rounded-full pl-[26px] pr-1 py-1 flex items-center gap-3 justify-between bg-input-bg max-w-[460px] w-full shadow-[0_4px_4px_rgba(255,255,255,0.2)]">
      <input
        placeholder={placeholder}
        type="text"
        value={query}
        onChange={e => setQuery(e.target.value)}
        className="text-[16px] flex-1 focus:outline-0 min-w-50px" />
      <button type="submit" className="cursor-pointer bg-primary rounded-full flex items-center justify-center py-2 pl-2.5 pr-1.5">
        <img src={searchIcon} alt="search icon" className="w-6 h-6" />
      </button>
    </div>

  );
}

export default Search;
