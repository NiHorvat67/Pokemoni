const Input = ({ placeholder }: { placeholder: string }) => {
  return (
    <>
      <input
        type="text"
        placeholder={placeholder}
        className="font-inter text-[16px] bg-input-bg rounded-[8px] text-black px-5.5 py-3 min-w-[300px] max-w-[350px] w-full"
      />
    </>
  );
}

export default Input;
