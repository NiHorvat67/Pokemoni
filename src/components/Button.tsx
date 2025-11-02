import { arrowIcon } from "@/assets/icons";

const Button = ({ text, icon, onClick, long = true, className = "", submit = false }: { text: string, icon: boolean, onClick: any, long: boolean, className?: string, submit?: boolean }) => {
  return (
    <>
      <button type={submit ? "submit" : "button"} onClick={onClick} className={`${long ? 'max-w-[350px] w-full' : ''} ${className} font-inter text-[16px] bg-primary font-medium rounded-[8px] text-black px-5.5 py-3 flex items-center justify-between gap-2 hover:shadow-[0_0_15px_#00FA55] transition-shadow duration-300 cursor-pointer`}
      >
        {text} {icon && <img src={arrowIcon} alt="arrow icon" className="h-[24px] w-[24px]" />}
      </button>
    </>
  );
}

export default Button;
