import { arrowIcon } from "@/assets/icons";

const Button = ({ text, icon, onClick, long = true, className = "" }: { text: string, icon: boolean, onClick: any, long: boolean, className?: string }) => {
  return (
    <>
      <button onClick={onClick} className={`${long ? 'min-w-[300px] max-w-[350px] w-full' : ''} ${className} font-inter text-[16px] bg-primary font-medium rounded-[8px] text-black px-5.5 py-3 flex items-center justify-between gap-2`}
      >
        {text} {icon && <img src={arrowIcon} alt="arrow icon" className="h-[24px] w-[24px]" />}
      </button>
    </>
  );
}

export default Button;
