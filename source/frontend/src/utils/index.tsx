

function generatePriceRanges(amount: number, step: number, start: number = 0) {
  return Array.from({ length: amount + start }, (_, i) => ({ start: (i + start) * step, end: (i + start + 1) * step }))
}


function checkAppendSearchParams(params: URLSearchParams, name: string, value: any) {
  if (value) {
    params.append(name, value)
  }
}

export {
  generatePriceRanges,
  checkAppendSearchParams
}


