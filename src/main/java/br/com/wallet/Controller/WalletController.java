package br.com.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.wallet.dto.DepositRequest;
import br.com.wallet.dto.TransferRequest;
import br.com.wallet.model.Wallet;
import br.com.wallet.repository.WalletRepository;
import br.com.wallet.service.WalletService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final WalletRepository walletRepo;

    public WalletController(WalletService walletService, WalletRepository walletRepo) {
        this.walletService = walletService;
        this.walletRepo = walletRepo;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest req) {
        walletService.deposit(req.getUserId(), req.getAmount());
        return ResponseEntity.ok("Depósito realizado com sucesso");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest req) {
        walletService.transfer(req.getFromUserId(), req.getToUserId(), req.getAmount());
        return ResponseEntity.ok("Transferência realizada com sucesso");
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> balance(@PathVariable Long userId) {
        Wallet w = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada"));
        return ResponseEntity.ok(w.getBalance());
    }
}
