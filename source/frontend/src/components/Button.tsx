import { arrowIcon } from "@/assets/icons";

const Button = ({ text, icon, onClick, long = true, className = "", submit = false, disabled = false }: { text: string, icon: boolean, onClick: () => void, long?: boolean, className?: string, submit?: boolean, disabled?: boolean }) => {
  return (
    <>
      <button
        disabled={disabled}
        type={submit ? "submit" : "button"}
        onClick={onClick}
        className={`${long ? 'max-w-[350px] w-full' : ''} ${className} ${disabled == true ? "bg-desc cursor-not-allowed" : "bg-primary text-black cursor-pointer"} font-inter text-[16px] font-medium rounded-[8px]  px-5 py-2 sm:px-5.5 sm:py-3 flex items-center justify-between gap-2 hover:shadow-[0_0_15px_#00FA55] transition-shadow duration-300 transition-color duration-300 group`}
      >
        {text} {(icon && !disabled) && <img src={arrowIcon} alt="arrow icon" className="h-[24px] w-[24px] transition-transform duration-100  group-hover:translate-x-1" />}
      </button>
    </>
  );
}

export default Button;
