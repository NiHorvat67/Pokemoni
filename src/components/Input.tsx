const Input = ({ placeholder, state, setState, type = "text" }: { placeholder: string, state: any, setState: any, type?: string }) => {
  return (
    <>
      <input
        type={type}
        value={state}
        onChange={(e) => setState(e.target.value)}
        placeholder={placeholder}
        className="font-inter text-[16px] bg-input-bg rounded-[8px] text-black px-5.5 py-3 max-w-[350px] w-full"
      />
    </>
  );
}

export default Input;
