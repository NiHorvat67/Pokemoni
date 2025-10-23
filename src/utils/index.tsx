

function generatePriceRanges(amount: number, step: number, start: number = 0) {
  return Array.from({ length: amount + start }, (_, i) => ({ start: (i + start) * step, end: (i + start + 1) * step }))


}

export {
  generatePriceRanges
}
