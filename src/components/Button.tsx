import { arrowIcon } from "@/assets/icons";

const Button = ({ text, icon }: { text: string, icon: boolean }) => {
  return (
    <>
      <button className="font-inter text-[16px] bg-primary font-medium rounded-[8px] text-black px-5.5 py-3 min-w-[300px] max-w-[350px] w-full flex items-center justify-between"
      >
        {text} {icon && <img src={arrowIcon} alt="arrow icon" className="h-[24px] w-[24px]" />}
      </button>
    </>
  );
}

export default Button;
