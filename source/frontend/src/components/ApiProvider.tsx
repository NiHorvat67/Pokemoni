
const ApiProvider = ({ icon, text }: { icon: any, text: string }) => {
  return (
    <>
      <a
        href="/api/login"
        className="cursor-pointer flex gap-2 items-center px-4 py-2  text-black font-inter font-medium rounded-[8px] bg-input-bg"
      >
        <img src={icon} alt="api provider icon" className="h-8 w-8" />
        {text}
      </a>
    </>
  );
}

export default ApiProvider;
