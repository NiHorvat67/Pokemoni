const Textarea = ({ placeholder, state, setState }: { placeholder: string, state: any, setState: any }) => {
  return (
    <textarea
      value={state}
      onChange={(e) => setState(e.target.value)}
      placeholder={placeholder}
      className="font-inter text-[16px] bg-input-bg rounded-[8px] text-black px-5.5 py-3 max-w-[350px] w-full h-[100px]"
    ></textarea>
  );
}

export default Textarea;
